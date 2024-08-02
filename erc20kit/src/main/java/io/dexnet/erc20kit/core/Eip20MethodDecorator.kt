package io.dexnet.erc20kit.core

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.contracts.ContractMethodFactories
import io.dexnet.ethereumkit.core.IMethodDecorator

class Eip20MethodDecorator(
    private val contractMethodFactories: ContractMethodFactories
) : IMethodDecorator {

    override fun contractMethod(input: ByteArray): ContractMethod? =
        contractMethodFactories.createMethodFromInput(input)

}
