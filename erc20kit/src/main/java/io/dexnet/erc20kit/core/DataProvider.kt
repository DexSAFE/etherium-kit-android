package io.dexnet.erc20kit.core

import io.dexnet.erc20kit.contract.BalanceOfMethod
import io.dexnet.ethereumkit.core.EthereumKit
import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.spv.core.toBigInteger
import io.reactivex.Single
import java.math.BigInteger

class DataProvider(
        private val ethereumKit: EthereumKit
) : IDataProvider {

    override fun getBalance(contractAddress: Address, address: Address): Single<BigInteger> {
        return ethereumKit.call(contractAddress, BalanceOfMethod(address).encodedABI())
                .map { it.sliceArray(IntRange(0, 31)).toBigInteger() }
    }

}
