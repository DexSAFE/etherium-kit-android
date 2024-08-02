package io.dexnet.ethereumkit.spv.net.les.messages

import io.dexnet.ethereumkit.spv.core.toLong
import io.dexnet.ethereumkit.spv.models.BlockHeader
import io.dexnet.ethereumkit.spv.net.IInMessage
import io.dexnet.ethereumkit.spv.rlp.RLP
import io.dexnet.ethereumkit.spv.rlp.RLPList

class BlockHeadersMessage(payload: ByteArray) : IInMessage {

    val requestID: Long
    val bv: Long
    val headers: List<BlockHeader>

    init {
        val headers = mutableListOf<BlockHeader>()
        val paramsList = RLP.decode2(payload)[0] as RLPList
        this.requestID = paramsList[0].rlpData.toLong()
        this.bv = paramsList[1].rlpData.toLong()
        val payloadList = paramsList[2] as RLPList
        for (i in 0 until payloadList.size) {
            val rlpData = payloadList[i] as RLPList
            headers.add(BlockHeader(rlpData))
        }
        this.headers = headers
    }

    override fun toString(): String {
        return "Headers [requestId: $requestID; bv: $bv; headers (${headers.size}): [${headers.joinToString(separator = ", ")}] ]"
    }
}
