package io.dexnet.uniswapkit.v3

import io.dexnet.uniswapkit.models.Fraction
import io.dexnet.uniswapkit.models.TradeType
import io.dexnet.uniswapkit.v3.pool.PoolManager
import io.dexnet.uniswapkit.v3.quoter.BestTrade
import java.math.BigDecimal

class PriceImpactManager(private val poolManager: PoolManager) {

    suspend fun getPriceImpact(bestTrade: BestTrade): BigDecimal? {
        val tradePrice = Fraction(bestTrade.amountIn, bestTrade.amountOut)

        val poolPrices = when (bestTrade.tradeType) {
            TradeType.ExactIn -> bestTrade.swapPath.items.map {
                poolManager.getPoolPrice(it.token2, it.token1, it.fee)
            }
            TradeType.ExactOut -> bestTrade.swapPath.items.map {
                poolManager.getPoolPrice(it.token1, it.token2, it.fee)
            }
        }

        val marketPrice = poolPrices.reduce { acc, fraction ->
            acc * fraction
        }

        val priceImpact = (tradePrice - marketPrice) / tradePrice * Fraction(BigDecimal(100))

        return priceImpact.toBigDecimal(2)
    }
}
