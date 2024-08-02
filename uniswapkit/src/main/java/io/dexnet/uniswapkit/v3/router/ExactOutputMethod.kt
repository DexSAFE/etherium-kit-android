package io.dexnet.uniswapkit.v3.router

import io.dexnet.ethereumkit.contracts.ContractMethod
import io.dexnet.ethereumkit.contracts.ContractMethodFactory
import io.dexnet.ethereumkit.contracts.ContractMethodHelper
import io.dexnet.ethereumkit.core.hexStringToByteArray
import io.dexnet.ethereumkit.models.Address
import io.dexnet.uniswapkit.v3.SwapPath
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.DynamicBytes
import org.web3j.abi.datatypes.DynamicStruct
import org.web3j.abi.datatypes.generated.Uint256
import java.math.BigInteger

class ExactOutputMethod(
    val path: ByteArray,
    val recipient: Address,
    val amountOut: BigInteger,
    val amountInMaximum: BigInteger,
) : ContractMethod() {
    override val methodSignature =
        io.dexnet.uniswapkit.v3.router.ExactOutputMethod.Companion.methodSignature
    override fun getArguments() = throw Exception("This class has its own implementation of encodedABI()")

    override fun encodedABI(): ByteArray {
        val function = org.web3j.abi.datatypes.Function(
            "exactOutput",
            listOf(
                DynamicStruct(
                    DynamicBytes(path),
                    org.web3j.abi.datatypes.Address(recipient.hex),
                    Uint256(amountOut),
                    Uint256(amountInMaximum)
                )
            ),
            listOf()
        )

        return FunctionEncoder.encode(function).hexStringToByteArray()
    }

    companion object {
        private const val methodSignature = "exactOutput((bytes,address,uint256,uint256))"
    }

    class Factory : ContractMethodFactory {
        override val methodId = ContractMethodHelper.getMethodId(io.dexnet.uniswapkit.v3.router.ExactOutputMethod.Companion.methodSignature)

        override fun createMethod(inputArguments: ByteArray): ContractMethod {
            val argumentTypes = listOf(
                ContractMethodHelper.DynamicStruct(
                    listOf(
                        ByteArray::class,
                        Address::class,
                        BigInteger::class,
                        BigInteger::class
                    )
                ),
            )
            val dynamicStruct = ContractMethodHelper.decodeABI(inputArguments, argumentTypes)

            val parsedArguments = dynamicStruct.first() as List<Any>

            return io.dexnet.uniswapkit.v3.router.ExactOutputMethod(
                path = parsedArguments[0] as ByteArray,
                recipient = parsedArguments[1] as Address,
                amountOut = parsedArguments[2] as BigInteger,
                amountInMaximum = parsedArguments[3] as BigInteger,
            )
        }
    }
}
