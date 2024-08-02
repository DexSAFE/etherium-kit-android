package io.dexnet.nftkit.decorations

import io.dexnet.ethereumkit.decorations.TransactionDecoration
import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.models.TransactionTag
import io.dexnet.nftkit.models.TokenInfo
import java.math.BigInteger

class OutgoingEip721Decoration(
    val contractAddress: Address,
    val to: Address,
    val tokenId: BigInteger,
    val sentToSelf: Boolean,
    val tokenInfo: TokenInfo?,
) : TransactionDecoration() {

    override fun tags(): List<String> =
        listOf(contractAddress.hex, EIP721_TRANSFER, TransactionTag.tokenOutgoing(contractAddress.hex), TransactionTag.OUTGOING)

    companion object {
        const val EIP721_TRANSFER = "eip721Transfer"
    }
}