package io.dexnet.erc20kit.contract

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.models.Address
import java.math.BigInteger

class ApproveMethod(val spender: Address, val value: BigInteger) : ContractMethod() {

    override val methodSignature =
        io.dexnet.erc20kit.contract.ApproveMethod.Companion.methodSignature
    override fun getArguments() = listOf(spender, value)

    companion object {
        const val methodSignature = "approve(address,uint256)"
    }

}
