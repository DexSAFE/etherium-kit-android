package io.dexnet.nftkit.contracts

import io.dexnet.ethereumkit.contracts.ContractMethodFactories

object Eip1155ContractMethodFactories : ContractMethodFactories() {
    init {
        registerMethodFactories(listOf(io.dexnet.nftkit.contracts.Eip1155SafeTransferFromMethodFactory()))
    }
}