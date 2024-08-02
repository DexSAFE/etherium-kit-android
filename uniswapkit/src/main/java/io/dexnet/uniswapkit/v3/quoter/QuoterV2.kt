package io.dexnet.uniswapkit.v3.quoter

import io.dexnet.ethereumkit.core.EthereumKit
import io.dexnet.ethereumkit.models.Address
import io.dexnet.ethereumkit.models.Chain
import io.dexnet.ethereumkit.spv.core.toBigInteger
import io.dexnet.uniswapkit.TradeError
import io.dexnet.uniswapkit.models.DexType
import io.dexnet.uniswapkit.models.Token
import io.dexnet.uniswapkit.models.TradeType
import io.dexnet.uniswapkit.v3.FeeAmount
import io.dexnet.uniswapkit.v3.SwapPath
import io.dexnet.uniswapkit.v3.SwapPathItem
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.rx2.await
import java.math.BigInteger
import kotlin.coroutines.coroutineContext

class QuoterV2(
    private val ethereumKit: EthereumKit,
    private val weth: Token,
    dexType: DexType
) {

    private val feeAmounts = FeeAmount.sorted(dexType)

    private val quoterAddress = when (dexType) {
        DexType.Uniswap -> getUniswapQuoterAddress(ethereumKit.chain)
        DexType.PancakeSwap -> getPancakeSwapQuoterAddress(ethereumKit.chain)
    }

    private fun getUniswapQuoterAddress(chain: Chain) = when (chain) {
        Chain.Ethereum,
        Chain.Polygon,
        Chain.Optimism,
        Chain.ArbitrumOne,
        Chain.EthereumGoerli -> "0x61fFE014bA17989E743c5F6cB21bF9697530B21e"
        Chain.BinanceSmartChain -> "0x78D78E420Da98ad378D7799bE8f4AF69033EB077"
        else -> throw IllegalStateException("Not supported Uniswap chain ${ethereumKit.chain}")
    }

    private fun getPancakeSwapQuoterAddress(chain: Chain) = when (chain) {
        Chain.BinanceSmartChain,
        Chain.Ethereum -> "0xB048Bbc1Ee6b733FFfCFb9e9CeF7375518e25997"
        else -> throw IllegalStateException("Not supported PancakeSwap chain ${ethereumKit.chain}")
    }

    suspend fun bestTradeExactIn(
        tokenIn: Token,
        tokenOut: Token,
        amountIn: BigInteger
    ): BestTrade {
        quoteExactInputSingle(tokenIn, tokenOut, amountIn)?.let {
            return it
        }
        quoteExactInputMultihop(tokenIn, tokenOut, amountIn)?.let {
            return it
        }
        throw TradeError.TradeNotFound()
    }

    private suspend fun quoteExactInputSingle(
        tokenIn: Token,
        tokenOut: Token,
        amountIn: BigInteger
    ): BestTrade? {
        val sqrtPriceLimitX96 = BigInteger.ZERO

        val amounts = feeAmounts.mapNotNull { fee ->
            coroutineContext.ensureActive()
            try {
                val callResponse = ethCall(
                    contractAddress = Address(quoterAddress),
                    data = QuoteExactInputSingleMethod(
                        tokenIn = tokenIn.address,
                        tokenOut = tokenOut.address,
                        fee = fee.value,
                        amountIn = amountIn,
                        sqrtPriceLimitX96 = sqrtPriceLimitX96
                    ).encodedABI()
                )

                val amountOut = callResponse.sliceArray(IntRange(0, 31)).toBigInteger()
                BestTrade(
                    tradeType = TradeType.ExactIn,
                    swapPath = SwapPath(listOf(SwapPathItem(tokenIn.address, tokenOut.address, fee))),
                    amountIn = amountIn,
                    amountOut = amountOut,
                    tokenIn = tokenIn,
                    tokenOut = tokenOut
                )
            } catch (t: Throwable) {
                null
            }
        }

        return amounts.maxByOrNull { it.amountOut }
    }

    private suspend fun quoteExactInputMultihop(
        tokenIn: Token,
        tokenOut: Token,
        amountIn: BigInteger
    ): BestTrade? {

        val swapInToWeth = quoteExactInputSingle(
            tokenIn = tokenIn,
            tokenOut = weth,
            amountIn = amountIn
        ) ?: return null

        val swapWethToOut = quoteExactInputSingle(
            tokenIn = weth,
            tokenOut = tokenOut,
            amountIn = swapInToWeth.amountOut
        ) ?: return null

        val path = SwapPath(swapInToWeth.swapPath.items + swapWethToOut.swapPath.items)

        coroutineContext.ensureActive()
        return try {
            val callResponse = ethCall(
                contractAddress = Address(quoterAddress),
                data = QuoteExactInputMethod(
                    path = path,
                    amountIn = amountIn,
                ).encodedABI()
            )

            val amountOut = callResponse.sliceArray(IntRange(0, 31)).toBigInteger()
            BestTrade(
                tradeType = TradeType.ExactIn,
                swapPath = path,
                amountIn = amountIn,
                amountOut = amountOut,
                tokenIn = tokenIn,
                tokenOut = tokenOut
            )
        } catch (t: Throwable) {
            null
        }
    }

    suspend fun bestTradeExactOut(
        tokenIn: Token,
        tokenOut: Token,
        amountOut: BigInteger
    ): BestTrade {
        quoteExactOutputSingle(tokenIn, tokenOut, amountOut)?.let {
            return it
        }
        quoteExactOutputMultihop(tokenIn, tokenOut, amountOut)?.let {
            return it
        }
        throw TradeError.TradeNotFound()
    }

    private suspend fun quoteExactOutputSingle(
        tokenIn: Token,
        tokenOut: Token,
        amountOut: BigInteger
    ): BestTrade? {
        val sqrtPriceLimitX96 = BigInteger.ZERO

        val amounts = feeAmounts.mapNotNull { fee ->
            coroutineContext.ensureActive()
            try {
                val callResponse = ethCall(
                    contractAddress = Address(quoterAddress),
                    data = QuoteExactOutputSingleMethod(
                        tokenIn = tokenIn.address,
                        tokenOut = tokenOut.address,
                        fee = fee.value,
                        amountOut = amountOut,
                        sqrtPriceLimitX96 = sqrtPriceLimitX96
                    ).encodedABI()
                )

                val amountIn = callResponse.sliceArray(IntRange(0, 31)).toBigInteger()
                BestTrade(
                    tradeType = TradeType.ExactOut,
                    swapPath = SwapPath(listOf(SwapPathItem(tokenOut.address, tokenIn.address, fee))),
                    amountIn = amountIn,
                    amountOut = amountOut,
                    tokenIn = tokenIn,
                    tokenOut = tokenOut
                )
            } catch (t: Throwable) {
                null
            }
        }

        return amounts.minByOrNull { it.amountIn }
    }

    private suspend fun quoteExactOutputMultihop(
        tokenIn: Token,
        tokenOut: Token,
        amountOut: BigInteger
    ): BestTrade? {
        val swapWethToOut = quoteExactOutputSingle(
            tokenIn = weth,
            tokenOut = tokenOut,
            amountOut = amountOut
        ) ?: return null

        val swapInToWeth = quoteExactOutputSingle(
            tokenIn = tokenIn,
            tokenOut = weth,
            amountOut = swapWethToOut.amountIn
        ) ?: return null

        val path = SwapPath(swapWethToOut.swapPath.items + swapInToWeth.swapPath.items)

        coroutineContext.ensureActive()
        return try {
            val callResponse = ethCall(
                contractAddress = Address(quoterAddress),
                data = QuoteExactOutputMethod(
                    path = path,
                    amountOut = amountOut,
                ).encodedABI()
            )

            val amountIn = callResponse.sliceArray(IntRange(0, 31)).toBigInteger()
            BestTrade(
                tradeType = TradeType.ExactOut,
                swapPath = path,
                amountIn = amountIn,
                amountOut = amountOut,
                tokenIn = tokenIn,
                tokenOut = tokenOut
            )
        } catch (t: Throwable) {
            null
        }
    }

    private suspend fun ethCall(contractAddress: Address, data: ByteArray): ByteArray {
        return ethereumKit.call(contractAddress, data).await()
    }
}
