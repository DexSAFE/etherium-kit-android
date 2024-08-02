package io.dexnet.nftkit.contracts

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.contracts.ContractMethodFactory
import io.dexnet.ethereumkit.contracts.ContractMethodHelper
import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.spv.core.toBigInteger

class Eip1155SafeTransferFromMethodFactory : ContractMethodFactory {
    override val methodId = ContractMethodHelper.getMethodId(io.dexnet.nftkit.contracts.Eip1155SafeTransferFromMethod.Companion.methodSignature)

    override fun createMethod(inputArguments: ByteArray): ContractMethod {
        val from = Address(inputArguments.copyOfRange(12, 32))
        val to = Address(inputArguments.copyOfRange(44, 64))
        val tokenId = inputArguments.copyOfRange(64, 96).toBigInteger()
        val value = inputArguments.copyOfRange(96, 128).toBigInteger()
        val data = inputArguments.copyOfRange(128, 160)

        return io.dexnet.nftkit.contracts.Eip1155SafeTransferFromMethod(
            from,
            to,
            tokenId,
            value,
            data
        )
    }
}