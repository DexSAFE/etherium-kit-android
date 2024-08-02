package io.dexnet.ethereumkit.spv.net.tasks

import io.dexnet.ethereumkit.spv.core.ITask
import io.dexnet.ethereumkit.models.RawTransaction
import io.dexnet.ethereumkit.models.Signature

class SendTransactionTask(val sendId: Int,
                          val rawTransaction: RawTransaction,
                          val signature: Signature) : ITask
