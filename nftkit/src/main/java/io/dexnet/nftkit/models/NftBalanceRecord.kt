package io.dexnet.nftkit.models

import androidx.room.Entity
import io.dexnet.ethereumkit.models.Address
import java.math.BigInteger

@Entity(primaryKeys = ["contractAddress", "tokenId"])
data class NftBalanceRecord(
    val type: NftType,
    val contractAddress: Address,
    val tokenId: BigInteger,
    val tokenName: String,
    val balance: Int,
    val synced: Boolean
) {
    constructor(nftBalance: NftBalance) : this(
        nftBalance.nft.type,
        nftBalance.nft.contractAddress,
        nftBalance.nft.tokenId,
        nftBalance.nft.tokenName,
        nftBalance.balance,
        nftBalance.synced
    )
}