package com.rnaufal.markets.gateways

import com.rnaufal.markets.domains.Market
import com.rnaufal.markets.gateways.repositories.MarketRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Component

interface MarketGateway {

    suspend fun save(market: Market): Market

    suspend fun findByRegistryCode(registryCode: String): Market?

    suspend fun delete(market: Market)

    suspend fun findById(id: String): Market?
}

@Component
class MongoDBMarketGateway(private val marketRepository: MarketRepository) : MarketGateway {

    override suspend fun save(market: Market): Market = marketRepository.save(market).awaitSingle()

    override suspend fun findByRegistryCode(registryCode: String) =
        marketRepository.findByRegistryCode(registryCode).awaitFirstOrNull()

    override suspend fun delete(market: Market) {
        marketRepository.delete(market).awaitFirstOrNull()
    }

    override suspend fun findById(id: String) = marketRepository.findById(id).awaitFirstOrNull()
}
