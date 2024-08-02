package io.dexnet.ethereumkit.spv.core

import io.dexnet.ethereumkit.core.ISpvStorage
import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.spv.models.AccountStateSpv
import io.dexnet.ethereumkit.spv.models.BlockHeader
import io.dexnet.ethereumkit.spv.net.handlers.AccountStateTaskHandler
import io.dexnet.ethereumkit.spv.net.tasks.AccountStateTask

class AccountStateSyncer(private val storage: ISpvStorage,
                         private val address: Address) : AccountStateTaskHandler.Listener {

    interface Listener {
        fun onUpdate(accountState: AccountStateSpv)
    }

    var listener: io.dexnet.ethereumkit.spv.core.AccountStateSyncer.Listener? = null

    fun sync(taskPerformer: io.dexnet.ethereumkit.spv.core.ITaskPerformer, blockHeader: BlockHeader) {
        taskPerformer.add(AccountStateTask(address, blockHeader))
    }

    override fun didReceive(accountState: AccountStateSpv, address: Address, blockHeader: BlockHeader) {
        storage.saveAccountSate(accountState)
        listener?.onUpdate(accountState)
    }

}
