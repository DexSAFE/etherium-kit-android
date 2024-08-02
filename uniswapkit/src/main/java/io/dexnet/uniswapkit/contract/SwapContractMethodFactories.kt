package io.dexnet.uniswapkit.contract

import io.dexnet.ethereumkit.contracts.ContractMethodFactories

object SwapContractMethodFactories : ContractMethodFactories() {
    init {
        val swapContractMethodFactories = listOf(
            io.dexnet.uniswapkit.contract.SwapETHForExactTokensMethodFactory,
            io.dexnet.uniswapkit.contract.SwapExactETHForTokensMethodFactory,
            io.dexnet.uniswapkit.contract.SwapExactETHForTokensSupportingFeeOnTransferTokensMethodFactory,
            io.dexnet.uniswapkit.contract.SwapExactTokensForETHMethodFactory,
            io.dexnet.uniswapkit.contract.SwapExactTokensForETHSupportingFeeOnTransferTokensMethodFactory,
            io.dexnet.uniswapkit.contract.SwapExactTokensForTokensMethodFactory,
            io.dexnet.uniswapkit.contract.SwapExactTokensForTokensSupportingFeeOnTransferTokensMethodFactory,
            io.dexnet.uniswapkit.contract.SwapTokensForExactETHMethodFactory,
            io.dexnet.uniswapkit.contract.SwapTokensForExactTokensMethodFactory
        )
        registerMethodFactories(swapContractMethodFactories)
    }
}
