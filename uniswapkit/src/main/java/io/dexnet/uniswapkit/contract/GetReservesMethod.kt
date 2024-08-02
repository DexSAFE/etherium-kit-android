package io.dexnet.uniswapkit.contract

import io.dexnet.ethereumkit.contracts.ContractMethod

class GetReservesMethod : ContractMethod() {

    override val methodSignature = "getReserves()"
    override fun getArguments() = listOf<Any>()

}
