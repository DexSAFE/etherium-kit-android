package io.dexnet.ethereumkit.spv.core

import io.dexnet.ethereumkit.core.ISpvStorage
import io.dexnet.ethereumkit.spv.models.BlockHeader
import io.dexnet.ethereumkit.spv.net.BlockHelper
import io.dexnet.ethereumkit.spv.net.BlockValidator
import io.dexnet.ethereumkit.spv.net.handlers.AnnouncedBlockHandler
import io.dexnet.ethereumkit.spv.net.handlers.BlockHeadersTaskHandler
import io.dexnet.ethereumkit.spv.net.handlers.HandshakeTaskHandler
import io.dexnet.ethereumkit.spv.net.tasks.BlockHeadersTask
import java.util.logging.Logger

class BlockSyncer(private val storage: ISpvStorage,
                  private val blockHelper: BlockHelper,
                  private val validator: BlockValidator,
                  private val headersLimit: Int = 50) : BlockHeadersTaskHandler.Listener, HandshakeTaskHandler.Listener, AnnouncedBlockHandler.Listener {

    interface Listener {
        fun onSuccess(taskPerformer: io.dexnet.ethereumkit.spv.core.ITaskPerformer, lastBlockHeader: BlockHeader)
        fun onFailure(error: Throwable)

        fun onUpdate(lastBlockHeader: BlockHeader)
    }

    private val logger = Logger.getLogger("BlockSyncer")

    var listener: io.dexnet.ethereumkit.spv.core.BlockSyncer.Listener? = null
    private var syncing = false

    override fun didReceive(peer: io.dexnet.ethereumkit.spv.core.IPeer, blockHeaders: List<BlockHeader>, blockHeader: BlockHeader, reverse: Boolean) {
        try {
            if (reverse) {
                handleFork(peer, blockHeaders, blockHeader)
            } else {
                handleBlockHeaders(peer, blockHeaders, blockHeader)
            }
        } catch (error: Exception) {
            when (error) {
                is BlockValidator.ForkDetected -> {

                    logger.info("Fork detected! Requesting reversed headers for block ${blockHeader.height}")

                    peer.add(BlockHeadersTask(blockHeader, headersLimit, true))
                }
                else -> {
                    syncing = false
                    listener?.onFailure(error)
                }
            }
        }
    }

    override fun didCompleteHandshake(peer: io.dexnet.ethereumkit.spv.core.IPeer, bestBlockHash: ByteArray, bestBlockHeight: Long) {
        onUpdate(peer, bestBlockHash, bestBlockHeight)
    }

    override fun didAnnounce(peer: io.dexnet.ethereumkit.spv.core.IPeer, blockHash: ByteArray, blockHeight: Long) {
        onUpdate(peer, blockHash, blockHeight)
    }

    private fun onUpdate(taskPerformer: io.dexnet.ethereumkit.spv.core.ITaskPerformer, bestBlockHash: ByteArray, bestBlockHeight: Long) {
        if (syncing) {
            logger.info("BlockSyncer: already syncing")
            return
        }

        val lastBlockHeader = blockHelper.lastBlockHeader

        if (lastBlockHeader.height > bestBlockHeight || (lastBlockHeader.height == bestBlockHeight && lastBlockHeader.hashHex.contentEquals(bestBlockHash))) {
            logger.info("BlockSyncer: no sync required")
            return
        }

        syncing = true

        taskPerformer.add(BlockHeadersTask(lastBlockHeader, headersLimit))
    }

    @Throws(io.dexnet.ethereumkit.spv.core.BlockSyncer.PeerError::class)
    private fun handleFork(taskPerformer: io.dexnet.ethereumkit.spv.core.ITaskPerformer, blockHeaders: List<BlockHeader>, fromBlockHeader: BlockHeader) {
        logger.info("Received reversed block headers")

        val localHeaders = storage.getBlockHeadersReversed(fromBlockHeader.height, blockHeaders.size)

        val forkedHeader = localHeaders.firstOrNull { localHeader ->
            blockHeaders.any { it.hashHex.contentEquals(localHeader.hashHex) && it.height == localHeader.height }
        } ?: throw io.dexnet.ethereumkit.spv.core.BlockSyncer.PeerError.InvalidForkedPeer()

        logger.info("Found forked block header ${forkedHeader.height}")

        taskPerformer.add(BlockHeadersTask(forkedHeader, headersLimit))
    }

    @Throws(BlockValidator.BlockValidationError::class)
    private fun handleBlockHeaders(taskPerformer: io.dexnet.ethereumkit.spv.core.ITaskPerformer, blockHeaders: List<BlockHeader>, blockHeader: BlockHeader) {
        validator.validate(blockHeaders, blockHeader)

        storage.saveBlockHeaders(blockHeaders)

        if (blockHeaders.size < headersLimit) {
            blockHeaders.lastOrNull()?.let { lastBlockHeader ->
                listener?.onSuccess(taskPerformer, lastBlockHeader)
                listener?.onUpdate(lastBlockHeader)
            }
            syncing = false

        } else {
            taskPerformer.add(BlockHeadersTask(blockHeaders.last(), headersLimit))
        }
    }

    open class PeerError : Exception() {
        class InvalidForkedPeer : io.dexnet.ethereumkit.spv.core.BlockSyncer.PeerError()
    }

}
