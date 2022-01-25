package com.rnaufal.markets.usecases

import com.rnaufal.markets.domains.Market
import com.rnaufal.markets.domains.SearchMarketParameters
import com.rnaufal.markets.exceptions.MarketNotFoundException
import com.rnaufal.markets.gateways.MarketGateway
import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class MarketService(private val marketGateway: MarketGateway) {

    companion object {
        private val logger = KotlinLogging.logger { }
    }

    suspend fun create(
        market: Market
    ): Market {
        return when (val maybeMarket = marketGateway.findByRegistryCode(market.registryCode)) {
            null -> marketGateway.save(market).also { logger.info { "Market $market created successfully" } }
            else -> maybeMarket
        }
    }

    suspend fun delete(registryCode: String) {
        marketGateway.delete(getByRegistryCode(registryCode)).also {
            logger.info { "Market with registry code $registryCode deleted" }
        }
    }

    suspend fun getById(id: String): Market {
        return marketGateway.findById(id)
            .also {
                logger.info { "Market $it found" }
            } ?: throw MarketNotFoundException("Market with id $id not found")
    }

    suspend fun update(updatedMarket: Market) =
        getByRegistryCode(updatedMarket.registryCode)
            .update(updatedMarket)
            .run {
                marketGateway.save(this)
            }.also {
                logger.info { "Market $it was updated" }
            }

    suspend fun search(
        searchMarketParameters: SearchMarketParameters,
        pageable: Pageable
    ) = marketGateway.search(searchMarketParameters, pageable)
        .also {
            logger.info { "Market search result for $searchMarketParameters found ${it.totalElements} markets" }
        }

    private suspend fun getByRegistryCode(code: String) =
        marketGateway.findByRegistryCode(code) ?: throw MarketNotFoundException(
            "Market with code $code not found"
        )
}
