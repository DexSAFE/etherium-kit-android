package io.dexnet.ethereumkit.sample.core

import android.content.Context
import io.dexnet.ethereumkit.core.EthereumKit
import io.dexnet.ethereumkit.core.signer.Signer
import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.models.FullTransaction
import io.dexnet.ethereumkit.models.GasPrice
import io.dexnet.ethereumkit.sample.modules.main.Erc20Token
import io.reactivex.Single
import java.math.BigDecimal

class Erc20Adapter(
    context: Context,
    token: Erc20Token,
    private val ethereumKit: EthereumKit,
    private val signer: Signer
) : Erc20BaseAdapter(context, token, ethereumKit) {

    override fun send(address: Address, amount: BigDecimal, gasPrice: GasPrice, gasLimit: Long): Single<FullTransaction> {
        val valueBigInteger = amount.movePointRight(decimals).toBigInteger()
        val transactionData = erc20Kit.buildTransferTransactionData(address, valueBigInteger)

        return ethereumKit
            .rawTransaction(transactionData, gasPrice, gasLimit)
            .flatMap { rawTransaction ->
                val signature = signer.signature(rawTransaction)
                ethereumKit.send(rawTransaction, signature)
            }
    }

    fun allowance(spenderAddress: Address): Single<BigDecimal> {
        return erc20Kit.getAllowanceAsync(spenderAddress).map { allowance -> allowance.toBigDecimal().movePointLeft(decimals) }
    }

}
