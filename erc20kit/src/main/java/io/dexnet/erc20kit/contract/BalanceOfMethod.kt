package io.dexnet.erc20kit.contract

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.models.Address

class BalanceOfMethod(val owner: Address) : ContractMethod() {

    override val methodSignature = "balanceOf(address)"
    override fun getArguments() = listOf(owner)

}
