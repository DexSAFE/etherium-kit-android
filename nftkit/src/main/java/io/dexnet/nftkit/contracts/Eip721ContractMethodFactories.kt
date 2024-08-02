package io.dexnet.nftkit.contracts

import io.dexnet.ethereumkit.contracts.ContractMethodFactories

object Eip721ContractMethodFactories : ContractMethodFactories() {
    init {
        registerMethodFactories(listOf(Eip721SafeTransferFromMethodFactory()))
    }
}