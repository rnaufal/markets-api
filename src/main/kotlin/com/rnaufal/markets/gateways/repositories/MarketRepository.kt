package com.rnaufal.markets.gateways.repositories

import com.rnaufal.markets.domains.Market
import com.rnaufal.markets.domains.SearchMarketParameters
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface MarketRepository : ReactiveMongoRepository<Market, String>, CustomMarketRepository {

    fun findByRegistryCode(registryCode: String): Mono<Market>
}

interface CustomMarketRepository {

    suspend fun searchByCriteria(
        searchMarketParameters: SearchMarketParameters,
        pageable: Pageable
    ): Page<Market>
}
