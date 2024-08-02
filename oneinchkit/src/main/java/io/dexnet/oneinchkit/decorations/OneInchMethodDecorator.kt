package io.dexnet.oneinchkit.decorations

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.core.IMethodDecorator
import io.dexnet.oneinchkit.contracts.OneInchContractMethodFactories

class OneInchMethodDecorator(private val contractMethodFactories: OneInchContractMethodFactories) : IMethodDecorator {

    override fun contractMethod(input: ByteArray): ContractMethod? =
        contractMethodFactories.createMethodFromInput(input)

}
