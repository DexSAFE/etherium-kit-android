package io.dexnet.ethereumkit.spv.net.tasks

import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.spv.core.ITask
import io.dexnet.ethereumkit.spv.models.BlockHeader

class AccountStateTask(val address: Address, val blockHeader: BlockHeader) : ITask
