package io.dexnet.erc20kit.decorations

import io.dexnet.ethereumkit.contracts.ContractEvent
import io.dexnet.ethereumkit.decorations.TransactionDecoration
import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.models.TransactionTag
import java.math.BigInteger

class ApproveEip20Decoration(
    val contractAddress: Address,
    val spender: Address,
    val value: BigInteger
) : TransactionDecoration() {

    override fun tags(): List<String> =
        listOf(contractAddress.hex, TransactionTag.EIP20_APPROVE)

    companion object {
        val signature = ContractEvent(
            "Approval",
            listOf(
                ContractEvent.Argument.Address,
                ContractEvent.Argument.Address,
                ContractEvent.Argument.Uint256
            )
        ).signature
    }
}
