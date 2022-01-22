package com.rnaufal.markets.gateways.repositories

import com.rnaufal.markets.domains.Market
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface MarketRepository : ReactiveMongoRepository<Market, String> {

    fun findByRegistryCode(registryCode: String): Mono<Market>
}
