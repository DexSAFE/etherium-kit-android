package io.dexnet.erc20kit.contract

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.models.Address
import java.math.BigInteger

class TransferMethod(val to: Address, val value: BigInteger) : ContractMethod() {

    override val methodSignature = TransferMethod.methodSignature
    override fun getArguments() = listOf(to, value)

    companion object {
        const val methodSignature = "transfer(address,uint256)"
    }

}
