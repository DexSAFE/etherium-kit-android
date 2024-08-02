package io.dexnet.uniswapkit.v3.router

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.contracts.ContractMethodFactories
import io.dexnet.ethereumkit.contracts.ContractMethodFactory
import io.dexnet.ethereumkit.contracts.ContractMethodHelper
import io.dexnet.ethereumkit.core.hexStringToByteArray
import io.dexnet.ethereumkit.core.toRawHexString
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.DynamicBytes
import org.web3j.abi.datatypes.Function

class MulticallMethod(val methods: List<ContractMethod>) : ContractMethod() {
    override val methodSignature = Companion.methodSignature

    override fun encodedABI(): ByteArray {
        val function = Function(
            "multicall",
            listOf(
                DynamicArray(methods.map { DynamicBytes(it.encodedABI()) })
            ),
            listOf()
        )

        return FunctionEncoder.encode(function).hexStringToByteArray()
    }

    companion object {
        private const val methodSignature = "multicall(bytes[])"
    }

    class Factory(private val factories: ContractMethodFactories) : ContractMethodFactory {
        override val methodId = ContractMethodHelper.getMethodId(methodSignature)

        override fun createMethod(inputArguments: ByteArray): MulticallMethod {
            val decode = TypeDecoder
                .decodeDynamicArray(
                    inputArguments.copyOfRange(32, inputArguments.size).toRawHexString(),
                    0,
                    object : TypeReference<DynamicArray<DynamicBytes>>() {

                    }
                )

            val methods = decode.value.mapNotNull {
                factories.createMethodFromInput(it.value)
            }

            return MulticallMethod(methods)
        }
    }
}
