package io.dexnet.ethereumkit.models

import io.dexnet.ethereumkit.decorations.TransactionDecoration

class FullTransaction(
    val transaction: Transaction,
    val decoration: TransactionDecoration
)
