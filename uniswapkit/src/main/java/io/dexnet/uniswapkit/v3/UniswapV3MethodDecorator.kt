package io.dexnet.uniswapkit.v3

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.core.IMethodDecorator
import io.dexnet.uniswapkit.v3.contract.UniswapV3ContractMethodFactories

class UniswapV3MethodDecorator(private val contractMethodFactories: UniswapV3ContractMethodFactories) :
    IMethodDecorator {

    override fun contractMethod(input: ByteArray): ContractMethod? =
        contractMethodFactories.createMethodFromInput(input)

}
