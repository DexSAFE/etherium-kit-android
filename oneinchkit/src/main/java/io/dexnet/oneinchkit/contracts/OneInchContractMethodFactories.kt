package io.dexnet.oneinchkit.contracts

import io.dexnet.ethereumkit.contracts.ContractMethodFactories
import io.dexnet.oneinchkit.contracts.v4.SwapMethodFactoryV4
import io.dexnet.oneinchkit.contracts.v4.UnoswapMethodFactoryV4
import io.dexnet.oneinchkit.contracts.v4.UnparsedSwapMethodsFactoryV4
import io.dexnet.oneinchkit.contracts.v5.SwapMethodFactoryV5
import io.dexnet.oneinchkit.contracts.v5.UnoswapMethodFactoryV5
import io.dexnet.oneinchkit.contracts.v5.UnparsedSwapMethodsFactoryV5

object OneInchContractMethodFactories : ContractMethodFactories() {

    init {
        val swapContractMethodFactories = listOf(
            UnoswapMethodFactoryV5(),
            SwapMethodFactoryV5(),
            UnparsedSwapMethodsFactoryV5(),
            UnoswapMethodFactoryV4(),
            SwapMethodFactoryV4(),
            UnparsedSwapMethodsFactoryV4()
        )
        registerMethodFactories(swapContractMethodFactories)
    }

}
