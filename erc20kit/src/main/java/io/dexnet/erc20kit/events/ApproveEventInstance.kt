package io.dexnet.erc20kit.events

import io.dexnet.ethereumkit.contracts.ContractEventInstance
import io.dexnet.ethereumkit.models.Address
import java.math.BigInteger

class ApproveEventInstance(
    contractAddress: Address,
    val owner: Address,
    val spender: Address,
    val value: BigInteger
) : ContractEventInstance(contractAddress)
