package io.dexnet.erc20kit.contract

import io.dexnet.ethereumkit.contracts.ContractMethodFactory
import io.dexnet.ethereumkit.contracts.ContractMethodHelper
import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.spv.core.toBigInteger

object ApproveMethodFactory : ContractMethodFactory {

    override val methodId = ContractMethodHelper.getMethodId(ApproveMethod.Companion.methodSignature)

    override fun createMethod(inputArguments: ByteArray): ApproveMethod {
        val address = Address(
            inputArguments.copyOfRange(
                12,
                32
            )
        )
        val value = inputArguments.copyOfRange(32, 64).toBigInteger()

        return ApproveMethod(address, value)
    }

}
