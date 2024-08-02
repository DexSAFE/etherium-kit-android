package io.dexnet.uniswapkit.v3.quoter

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.uniswapkit.v3.SwapPath
import java.math.BigInteger

class QuoteExactInputMethod(
    val path: SwapPath,
    val amountIn: BigInteger,
) : ContractMethod() {

    override val methodSignature = "quoteExactInput(bytes,uint256)"
    override fun getArguments() = listOf(path.abiEncodePacked(), amountIn)

}
