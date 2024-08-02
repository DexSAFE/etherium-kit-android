package io.dexnet.ethereumkit.api.jsonrpc

import com.google.gson.reflect.TypeToken
import io.dexnet.ethereumkit.api.jsonrpc.models.RpcTransaction
import java.lang.reflect.Type

class GetTransactionByHashJsonRpc(
        @Transient val transactionHash: ByteArray
) : JsonRpc<RpcTransaction>(
        method = "eth_getTransactionByHash",
        params = listOf(transactionHash)
) {
    @Transient
    override val typeOfResult: Type = object : TypeToken<RpcTransaction>() {}.type
}
