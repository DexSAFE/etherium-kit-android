package io.dexnet.erc20kit.contract

import io.dexnet.ethereumkit.contracts.ContractMethod

class SymbolMethod: ContractMethod() {
    override var methodSignature = "symbol()"
    override fun getArguments() = listOf<Any>()
}
