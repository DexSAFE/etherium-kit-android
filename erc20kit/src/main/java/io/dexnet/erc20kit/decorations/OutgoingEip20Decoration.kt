package io.dexnet.erc20kit.decorations

import io.dexnet.erc20kit.events.TokenInfo
import io.dexnet.ethereumkit.decorations.TransactionDecoration
import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.models.TransactionTag
import java.math.BigInteger

class OutgoingEip20Decoration(
    val contractAddress: Address,
    val to: Address,
    val value: BigInteger,
    val sentToSelf: Boolean,
    val tokenInfo: TokenInfo?
) : TransactionDecoration() {

    override fun tags(): List<String> =
        listOf(contractAddress.hex, TransactionTag.EIP20_TRANSFER, TransactionTag.tokenOutgoing(contractAddress.hex), TransactionTag.OUTGOING)

}
