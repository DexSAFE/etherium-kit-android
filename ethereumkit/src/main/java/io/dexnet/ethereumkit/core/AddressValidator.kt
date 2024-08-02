package io.dexnet.ethereumkit.core

import io.dexnet.ethereumkit.utils.EIP55
import java.math.BigInteger

class AddressValidator {

    open class AddressValidationException(msg: String) : Exception(msg)
    class InvalidAddressLength(msg: String) : io.dexnet.ethereumkit.core.AddressValidator.AddressValidationException(msg)
    class InvalidAddressHex(msg: String) : io.dexnet.ethereumkit.core.AddressValidator.AddressValidationException(msg)
    class InvalidAddressChecksum(msg: String) : io.dexnet.ethereumkit.core.AddressValidator.AddressValidationException(msg)

    companion object {
        private const val ADDRESS_LENGTH_IN_HEX = 40

        @Throws(io.dexnet.ethereumkit.core.AddressValidator.AddressValidationException::class)
        fun validate(address: String) {
            val cleanAddress = address.stripHexPrefix()

            check(cleanAddress.length == io.dexnet.ethereumkit.core.AddressValidator.Companion.ADDRESS_LENGTH_IN_HEX) {
                throw io.dexnet.ethereumkit.core.AddressValidator.InvalidAddressLength("address: $address")
            }

            try {
                BigInteger(cleanAddress, 16)
            } catch (ex: NumberFormatException) {
                throw io.dexnet.ethereumkit.core.AddressValidator.InvalidAddressHex("address: $address")
            }

            if (io.dexnet.ethereumkit.core.AddressValidator.Companion.isMixedCase(cleanAddress)) {
                val checksumAddress = EIP55.format(cleanAddress).stripHexPrefix()
                check(checksumAddress == cleanAddress) {
                    throw io.dexnet.ethereumkit.core.AddressValidator.InvalidAddressChecksum("address: $address")
                }
            }
        }

        private fun isMixedCase(address: String): Boolean {
            var containsUpperCase = false
            var containsLowerCase = false

            address.forEach {
                when {
                    it.isUpperCase() -> containsUpperCase = true
                    it.isLowerCase() -> containsLowerCase = true
                }
            }
            return containsLowerCase && containsUpperCase
        }
    }

}
