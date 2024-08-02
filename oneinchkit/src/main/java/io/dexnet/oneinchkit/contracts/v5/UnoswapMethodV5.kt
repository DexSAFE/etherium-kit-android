package io.dexnet.oneinchkit.contracts.v5

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.models.Address
import java.math.BigInteger

class UnoswapMethodV5(
    val srcToken: Address,
    val amount: BigInteger,
    val minReturn: BigInteger,
    val params: List<BigInteger>
) : ContractMethod() {

    override val methodSignature = Companion.methodSignature

    override fun getArguments() = listOf(srcToken, amount, minReturn, params)

    companion object {
        val methodSignature = "unoswap(address,uint256,uint256,uint256[])"
    }

}
