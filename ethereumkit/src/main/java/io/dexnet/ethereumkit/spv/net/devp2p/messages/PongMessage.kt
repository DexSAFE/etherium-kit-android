package io.dexnet.ethereumkit.spv.net.devp2p.messages

import io.dexnet.ethereumkit.core.hexStringToByteArray
import io.dexnet.ethereumkit.spv.net.IInMessage
import io.dexnet.ethereumkit.spv.net.IOutMessage

class PongMessage() : IInMessage, IOutMessage {

    constructor(payload: ByteArray) : this()

    override fun encoded(): ByteArray {
        return payload
    }

    override fun toString(): String {
        return "Pong"
    }

    companion object {
        val payload = "C0".hexStringToByteArray()
    }
}
