package io.dexnet.nftkit.core

import io.dexnet.ethereumkit.contracts.ContractEventInstance
import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.core.ITransactionDecorator
import io.dexnet.ethereumkit.decorations.TransactionDecoration
import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.models.InternalTransaction
import io.dexnet.nftkit.contracts.Eip721SafeTransferFromMethod
import io.dexnet.nftkit.decorations.OutgoingEip721Decoration
import io.dexnet.nftkit.events.Eip721TransferEventInstance
import java.math.BigInteger

class Eip721TransactionDecorator(
    private val userAddress: Address
) : ITransactionDecorator {

    override fun decoration(
        from: Address?,
        to: Address?,
        value: BigInteger?,
        contractMethod: ContractMethod?,
        internalTransactions: List<InternalTransaction>,
        eventInstances: List<ContractEventInstance>
    ): TransactionDecoration? {
        if (from == null || to == null || value == null || contractMethod == null) return null

        return when {
            contractMethod is Eip721SafeTransferFromMethod && from == userAddress -> {
                OutgoingEip721Decoration(
                    contractAddress = to,
                    to = contractMethod.to,
                    tokenId = contractMethod.tokenId,
                    sentToSelf = contractMethod.to == userAddress,
                    tokenInfo = eventInstances.mapNotNull { it as? Eip721TransferEventInstance }.firstOrNull { it.contractAddress == to }?.tokenInfo
                )
            }
            else -> null
        }
    }
}