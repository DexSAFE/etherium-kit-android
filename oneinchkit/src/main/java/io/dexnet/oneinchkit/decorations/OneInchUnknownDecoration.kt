package io.dexnet.oneinchkit.decorations

import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.models.TransactionTag
import java.math.BigInteger

class OneInchUnknownDecoration(
    override val contractAddress: Address,
    val tokenAmountIn: io.dexnet.oneinchkit.decorations.OneInchUnknownDecoration.TokenAmount?,
    val tokenAmountOut: io.dexnet.oneinchkit.decorations.OneInchUnknownDecoration.TokenAmount?
) : io.dexnet.oneinchkit.decorations.OneInchDecoration(contractAddress) {

    class TokenAmount(val token: io.dexnet.oneinchkit.decorations.OneInchDecoration.Token, val value: BigInteger)

    override fun tags(): List<String> {
        val tags = super.tags().toMutableList()

        listOf(contractAddress.hex, TransactionTag.SWAP)

        if (tokenAmountIn != null) {
            tags.addAll(getTags(tokenAmountIn.token, TransactionTag.OUTGOING))
        }

        if (tokenAmountOut != null) {
            tags.addAll(getTags(tokenAmountOut.token, TransactionTag.INCOMING))
        }

        return tags
    }

}
