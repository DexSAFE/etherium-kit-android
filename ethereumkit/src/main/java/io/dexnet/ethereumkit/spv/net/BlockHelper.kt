package io.dexnet.ethereumkit.spv.net

import io.dexnet.ethereumkit.core.ISpvStorage
import io.dexnet.ethereumkit.network.INetwork
import io.dexnet.ethereumkit.spv.models.BlockHeader

class BlockHelper(val storage: ISpvStorage, val network: INetwork) {

    val lastBlockHeader: BlockHeader
        get() = storage.getLastBlockHeader() ?: network.checkpointBlock

}
