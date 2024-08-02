package io.dexnet.erc20kit.contract

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.models.Address

class AllowanceMethod(val owner: Address, val spender: Address) : ContractMethod() {

    override val methodSignature = "allowance(address,address)"
    override fun getArguments() = listOf(owner, spender)

}
