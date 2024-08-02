package io.dexnet.nftkit.core

import io.dexnet.nftkit.models.Nft
import io.dexnet.nftkit.models.NftType

interface ITransactionSyncerListener {
    fun didSync(nfts: List<Nft>, type: NftType)
}

interface IBalanceSyncManagerListener {
    fun didFinishSyncBalances()
}