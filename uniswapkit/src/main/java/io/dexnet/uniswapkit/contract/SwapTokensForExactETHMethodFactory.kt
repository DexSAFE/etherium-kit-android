package io.dexnet.uniswapkit.contract

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.contracts.ContractMethodFactory
import io.dexnet.ethereumkit.contracts.ContractMethodHelper
import io.dexnet.ethereumkit.models.Address
import java.math.BigInteger

object SwapTokensForExactETHMethodFactory : ContractMethodFactory {
    override val methodId = ContractMethodHelper.getMethodId(io.dexnet.uniswapkit.contract.SwapTokensForExactETHMethod.Companion.methodSignature)

    override fun createMethod(inputArguments: ByteArray): ContractMethod {
        val parsedArguments = ContractMethodHelper.decodeABI(inputArguments, listOf(BigInteger::class, BigInteger::class, List::class, Address::class, BigInteger::class))
        val amountOut = parsedArguments[0] as BigInteger
        val amountInMax = parsedArguments[1] as BigInteger
        val path = parsedArguments[2] as List<Address>
        val to = parsedArguments[3] as Address
        val deadline = parsedArguments[4] as BigInteger
        return io.dexnet.uniswapkit.contract.SwapTokensForExactETHMethod(
            amountOut,
            amountInMax,
            path,
            to,
            deadline
        )
    }

}
