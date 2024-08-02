package io.dexnet.ethereumkit.spv.net.tasks

import io.dexnet.ethereumkit.network.INetwork
import io.dexnet.ethereumkit.spv.core.ITask
import io.dexnet.ethereumkit.spv.models.BlockHeader
import java.math.BigInteger

class HandshakeTask(val peerId: String, network: INetwork, blockHeader: BlockHeader) : ITask {
    val networkId: Int = network.id
    val genesisHash: ByteArray = network.genesisBlockHash
    val headTotalDifficulty: BigInteger = blockHeader.totalDifficulty
    val headHash: ByteArray = blockHeader.hashHex
    val headHeight: Long = blockHeader.height
}
