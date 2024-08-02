package io.dexnet.ethereumkit.sample.core

import io.dexnet.ethereumkit.models.Address
import java.math.BigDecimal

class TransactionRecord(
    val transactionHash: String,
    val timestamp: Long,
    val isError: Boolean,
    var from: Address?,
    var to: Address?,
    val amount: BigDecimal?,
    val blockHeight: Long?,
    val transactionIndex: Int?,
    val decoration: String
)
