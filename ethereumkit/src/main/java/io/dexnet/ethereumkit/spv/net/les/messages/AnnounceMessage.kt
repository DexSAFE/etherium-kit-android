package io.dexnet.ethereumkit.spv.net.les.messages

import io.dexnet.ethereumkit.core.toHexString
import io.dexnet.ethereumkit.spv.core.toBigInteger
import io.dexnet.ethereumkit.spv.core.toLong
import io.dexnet.ethereumkit.spv.net.IInMessage
import io.dexnet.ethereumkit.spv.rlp.RLP
import io.dexnet.ethereumkit.spv.rlp.RLPList
import java.math.BigInteger

class AnnounceMessage(payload: ByteArray) : IInMessage {

    val blockHash: ByteArray
    val blockHeight: Long
    val blockTotalDifficulty: BigInteger
    val reorganizationDepth: Long

    init {
        val params = RLP.decode2(payload)[0] as RLPList
        blockHash = params[0].rlpData ?: byteArrayOf()
        blockHeight = params[1].rlpData.toLong()
        blockTotalDifficulty = params[2].rlpData.toBigInteger()
        reorganizationDepth = params[3].rlpData.toLong()
    }

    override fun toString(): String {
        return "Announce [blockHash: ${blockHash.toHexString()}; " +
                "blockHeight: $blockHeight; " +
                "blockTotalDifficulty: $blockTotalDifficulty; " +
                "reorganizationDepth: $reorganizationDepth]"
    }
}
