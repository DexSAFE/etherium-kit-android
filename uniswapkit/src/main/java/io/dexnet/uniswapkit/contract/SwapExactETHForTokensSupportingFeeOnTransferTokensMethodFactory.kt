package io.dexnet.uniswapkit.contract

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.contracts.ContractMethodFactory
import io.dexnet.ethereumkit.contracts.ContractMethodHelper
import io.dexnet.ethereumkit.models.Address
import java.math.BigInteger

object SwapExactETHForTokensSupportingFeeOnTransferTokensMethodFactory : ContractMethodFactory {

    override val methodId = ContractMethodHelper.getMethodId(io.dexnet.uniswapkit.contract.SwapExactETHForTokensMethod.Companion.methodSignature)

    override fun createMethod(inputArguments: ByteArray): ContractMethod {
        val parsedArguments = ContractMethodHelper.decodeABI(inputArguments, listOf(BigInteger::class, List::class, Address::class, BigInteger::class))
        val amountOutMin = parsedArguments[0] as BigInteger
        val path = parsedArguments[1] as List<Address>
        val to = parsedArguments[2] as Address
        val deadline = parsedArguments[3] as BigInteger
        return io.dexnet.uniswapkit.contract.SwapExactETHForTokensMethod(
            amountOutMin,
            path,
            to,
            deadline
        )
    }

}
