package io.dexnet.uniswapkit.v3.router

import io.dexnet.ethereumkit.contracts.ContractMethod

class RefundETHMethod : ContractMethod() {
    override val methodSignature = "refundETH()"
}

