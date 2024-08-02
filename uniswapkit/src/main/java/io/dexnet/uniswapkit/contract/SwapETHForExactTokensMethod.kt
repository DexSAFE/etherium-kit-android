package io.dexnet.uniswapkit.contract

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.models.Address
import java.math.BigInteger

class SwapETHForExactTokensMethod(
        val amountOut: BigInteger,
        val path: List<Address>,
        val to: Address,
        val deadline: BigInteger
) : ContractMethod() {

    override val methodSignature =
        io.dexnet.uniswapkit.contract.SwapETHForExactTokensMethod.Companion.methodSignature
    override fun getArguments() = listOf(amountOut, path, to, deadline)

    companion object {
        const val methodSignature = "swapETHForExactTokens(uint256,address[],address,uint256)"
    }

}
