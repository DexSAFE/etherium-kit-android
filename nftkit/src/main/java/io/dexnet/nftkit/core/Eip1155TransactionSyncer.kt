package io.dexnet.nftkit.core

import io.dexnet.ethereumkit.core.ITransactionProvider
import io.dexnet.ethereumkit.core.ITransactionSyncer
import io.dexnet.ethereumkit.models.ProviderEip1155Transaction
import io.dexnet.ethereumkit.models.Transaction
import io.dexnet.nftkit.models.Eip1155Event
import io.dexnet.nftkit.models.Nft
import io.dexnet.nftkit.models.NftType
import io.reactivex.Single

class Eip1155TransactionSyncer(
    private val transactionProvider: ITransactionProvider,
    private val storage: Storage
) : ITransactionSyncer {

    var listener: ITransactionSyncerListener? = null

    private fun handle(transactions: List<ProviderEip1155Transaction>) {
        if (transactions.isEmpty()) return

        val events = transactions.map { tx ->
            Eip1155Event(tx.hash, tx.blockNumber, tx.contractAddress, tx.from, tx.to, tx.tokenId, tx.tokenValue, tx.tokenName, tx.tokenSymbol)
        }

        storage.saveEip1155Events(events)

        val nfts = events.map { event ->
            Nft(
                NftType.Eip1155,
                contractAddress = event.contractAddress,
                tokenId = event.tokenId,
                tokenName = event.tokenName
            )
        }.distinct()

        listener?.didSync(nfts, NftType.Eip1155)
    }

    override fun getTransactionsSingle(): Single<Pair<List<Transaction>, Boolean>> {
        val lastTransactionBlockNumber = storage.lastEip1155Event()?.blockNumber ?: 0
        val initial: Boolean = lastTransactionBlockNumber == 0L

        return transactionProvider.getEip1155Transactions(lastTransactionBlockNumber + 1)
            .doOnSuccess { providerTokenTransactions -> handle(providerTokenTransactions) }
            .map { providerTokenTransactions ->
                val array = providerTokenTransactions.map { transaction ->
                    Transaction(
                        hash = transaction.hash,
                        timestamp = transaction.timestamp,
                        isFailed = false,
                        blockNumber = transaction.blockNumber,
                        transactionIndex = transaction.transactionIndex,
                        nonce = transaction.nonce,
                        gasPrice = transaction.gasPrice,
                        gasLimit = transaction.gasLimit,
                        gasUsed = transaction.gasUsed
                    )

                }
                Pair(array, initial)
            }
            .onErrorReturnItem(Pair(listOf(), initial))
    }
}