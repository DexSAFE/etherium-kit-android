package io.dexnet.ethereumkit.api.core

import io.dexnet.ethereumkit.api.jsonrpc.BlockNumberJsonRpc
import io.dexnet.ethereumkit.api.jsonrpc.JsonRpc
import io.dexnet.ethereumkit.core.EthereumKit
import io.dexnet.ethereumkit.network.ConnectionManager
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.concurrent.schedule

class ApiRpcSyncer(
    private val rpcApiProvider: io.dexnet.ethereumkit.api.core.IRpcApiProvider,
    private val connectionManager: ConnectionManager,
    private val syncInterval: Long,
) : io.dexnet.ethereumkit.api.core.IRpcSyncer {
    private val disposables = CompositeDisposable()
    private var isStarted = false
    private var timer: Timer? = null

    init {
        connectionManager.listener = object : ConnectionManager.Listener {
            override fun onConnectionChange() {
                handleConnectionChange()
            }
        }
    }

    //region IRpcSyncer
    override var listener: io.dexnet.ethereumkit.api.core.IRpcSyncerListener? = null
    override val source = "API ${rpcApiProvider.source}"
    override var state: io.dexnet.ethereumkit.api.core.SyncerState =
        io.dexnet.ethereumkit.api.core.SyncerState.NotReady(EthereumKit.SyncError.NotStarted())
        private set(value) {
            if (value != field) {
                field = value
                listener?.didUpdateSyncerState(value)
            }
        }

    override fun start() {
        isStarted = true

        handleConnectionChange()
    }

    override fun stop() {
        isStarted = false

        state =
            io.dexnet.ethereumkit.api.core.SyncerState.NotReady(EthereumKit.SyncError.NotStarted())
        disposables.clear()
        stopTimer()
    }

    override fun <T> single(rpc: JsonRpc<T>): Single<T> =
        rpcApiProvider.single(rpc)
    //endregion

    private fun handleConnectionChange() {
        if (!isStarted) return

        if (connectionManager.isConnected) {
            state = io.dexnet.ethereumkit.api.core.SyncerState.Ready
            startTimer()
        } else {
            state =
                io.dexnet.ethereumkit.api.core.SyncerState.NotReady(EthereumKit.SyncError.NoNetworkConnection())
            stopTimer()
        }
    }

    private fun startTimer() {
        timer = Timer().apply {
            schedule(0, syncInterval * 1000) {
                onFireTimer()
            }
        }
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    private fun onFireTimer() {
        rpcApiProvider.single(BlockNumberJsonRpc())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ lastBlockNumber ->
                    listener?.didUpdateLastBlockHeight(lastBlockNumber)
                }, {
                    state = io.dexnet.ethereumkit.api.core.SyncerState.NotReady(it)
                }).let {
                    disposables.add(it)
                }
    }

}
