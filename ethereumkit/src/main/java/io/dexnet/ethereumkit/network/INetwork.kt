package io.dexnet.ethereumkit.network

import io.dexnet.ethereumkit.spv.models.BlockHeader

interface INetwork {
    val id: Int
    val genesisBlockHash: ByteArray
    val checkpointBlock: BlockHeader
    val blockTime: Long
}