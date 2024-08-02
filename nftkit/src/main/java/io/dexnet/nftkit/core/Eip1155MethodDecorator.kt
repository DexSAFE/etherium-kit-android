package io.dexnet.nftkit.core

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.contracts.ContractMethodFactories
import io.dexnet.ethereumkit.core.IMethodDecorator

class Eip1155MethodDecorator(
    private val contractMethodFactories: ContractMethodFactories
) : IMethodDecorator {
    override fun contractMethod(input: ByteArray): ContractMethod? {
        return contractMethodFactories.createMethodFromInput(input)
    }
}