package io.dexnet.ethereumkit.contracts

import io.dexnet.ethereumkit.models.Address

open class ContractEventInstance(val contractAddress: Address) {

    open fun tags(userAddress: Address): List<String> = listOf()

}
