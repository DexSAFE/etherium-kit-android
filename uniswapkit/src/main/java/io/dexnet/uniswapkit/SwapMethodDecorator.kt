package io.dexnet.uniswapkit

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.core.IMethodDecorator
import io.dexnet.uniswapkit.contract.SwapContractMethodFactories

class SwapMethodDecorator(private val contractMethodFactories: io.dexnet.uniswapkit.contract.SwapContractMethodFactories) : IMethodDecorator {

    override fun contractMethod(input: ByteArray): ContractMethod? =
        contractMethodFactories.createMethodFromInput(input)

}
