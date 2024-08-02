package io.dexnet.ethereumkit.models

import io.dexnet.ethereumkit.core.AddressValidator
import io.dexnet.ethereumkit.core.hexStringToByteArray
import io.dexnet.ethereumkit.core.toHexString
import io.dexnet.ethereumkit.utils.EIP55

data class Address(var raw: ByteArray) {
    init {
        if (raw.size == 32) {
            raw = raw.copyOfRange(12, raw.size)
        }
        io.dexnet.ethereumkit.core.AddressValidator.validate(hex)
    }

    constructor(hex: String) : this(hex.hexStringToByteArray())

    val hex: String
        get() = raw.toHexString()

    val eip55: String
        get() = EIP55.format(hex)

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        return if (other is Address)
            raw.contentEquals(other.raw)
        else false
    }

    override fun hashCode(): Int {
        return raw.contentHashCode()
    }

    override fun toString(): String {
        return hex
    }

}
