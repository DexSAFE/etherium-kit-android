package io.dexnet.ethereumkit.spv.net.tasks

import io.dexnet.ethereumkit.spv.core.ITask
import io.dexnet.ethereumkit.spv.models.BlockHeader

class BlockHeadersTask(val blockHeader: BlockHeader, val limit: Int, val reverse: Boolean = false) : ITask