package com.rnaufal.markets.usecases

import com.rnaufal.markets.domains.Market
import com.rnaufal.markets.exceptions.MarketNotFoundException
import com.rnaufal.markets.gateways.MarketGateway
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class MarketService(private val marketGateway: MarketGateway) {

    companion object {
        private val logger = KotlinLogging.logger { }
    }

    suspend fun execute(
        market: Market
    ): Market {
        logger.info { "Creating market $market" }

        return when (val maybeMarket = marketGateway.findByRegistryCode(market.registryCode)) {
            null -> marketGateway.save(market).also { logger.info { "Market $market created successfully" } }
            else -> maybeMarket
        }
    }

    suspend fun delete(registryCode: String) {
        logger.info { "Deleting market by registry code $registryCode" }

        marketGateway.delete(findMarketByRegistryCode(registryCode))
    }

    suspend fun getById(id: String) =
        marketGateway.findById(id) ?: throw MarketNotFoundException("Market with id $id not found")

    private suspend fun findMarketByRegistryCode(code: String) =
        marketGateway.findByRegistryCode(code) ?: throw MarketNotFoundException(
            "Market with code $code not found"
        )
}
