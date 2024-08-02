package io.dexnet.erc20kit.contract

import io.dexnet.ethereumkit.contracts.ContractMethodFactories

object Eip20ContractMethodFactories : ContractMethodFactories() {

    init {
        registerMethodFactories(listOf(TransferMethodFactory,
            io.dexnet.erc20kit.contract.ApproveMethodFactory
        ))
    }

}
