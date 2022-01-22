package com.rnaufal.markets.usecases

import com.rnaufal.markets.domains.Market
import com.rnaufal.markets.gateways.MarketGateway
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CreateMarket(private val marketGateway: MarketGateway) {

    companion object {
        private val logger = KotlinLogging.logger { }
    }

    suspend fun execute(
        market: Market
    ): Market {
        return when (val maybeMarket = marketGateway.findByRegistryCode(market.registryCode)) {
            null -> marketGateway.save(market).also { logger.info { "Market $market created successfully" } }
            else -> maybeMarket
        }
    }
}
