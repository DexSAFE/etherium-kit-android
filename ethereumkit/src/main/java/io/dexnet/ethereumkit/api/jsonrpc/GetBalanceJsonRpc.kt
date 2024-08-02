package io.dexnet.ethereumkit.api.jsonrpc

import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.models.DefaultBlockParameter
import java.math.BigInteger

class GetBalanceJsonRpc(
        @Transient val address: Address,
        @Transient val defaultBlockParameter: DefaultBlockParameter
) : JsonRpc<BigInteger>(
        method = "eth_getBalance",
        params = listOf(address, defaultBlockParameter)
) {
    @Transient
    override val typeOfResult = BigInteger::class.java
}
