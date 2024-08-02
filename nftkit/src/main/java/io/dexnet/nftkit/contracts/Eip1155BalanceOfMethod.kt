package io.dexnet.nftkit.contracts

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.models.Address
import java.math.BigInteger

class Eip1155BalanceOfMethod(
    private val owner: Address,
    private val tokenId: BigInteger
) : ContractMethod() {

    override val methodSignature =
        io.dexnet.nftkit.contracts.Eip1155BalanceOfMethod.Companion.methodSignature

    override fun getArguments() = listOf(owner, tokenId)

    companion object {
        const val methodSignature = "balanceOf(address,uint256)"
    }
}