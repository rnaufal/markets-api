package com.rnaufal.markets.gateways.repositories

import com.rnaufal.markets.domains.Market
import com.rnaufal.markets.domains.SearchMarketParameters
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class CustomMarketRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : CustomMarketRepository {

    override suspend fun searchByCriteria(
        searchMarketParameters: SearchMarketParameters,
        pageable: Pageable,
    ): Page<Market> {
        val criteria = buildCriteria(searchMarketParameters)
        val query = Query(criteria).with(pageable)

        val marketsCount = fetchMarketsCount(query)
        val markets = fetchMarkets(query)

        return PageableExecutionUtils.getPage(markets, pageable) { marketsCount }
    }

    private suspend fun fetchMarketsCount(query: Query) = reactiveMongoTemplate.count(
        Query.of(query).limit(-1).skip(-1),
        Market::class.java
    ).awaitSingle()

    private suspend fun fetchMarkets(query: Query) = reactiveMongoTemplate
        .find(query, Market::class.java)
        .collectList()
        .awaitSingle()

    private fun buildCriteria(
        searchMarketParameters: SearchMarketParameters,
    ): Criteria {
        val criteria = mapOf(
            "district" to searchMarketParameters.district,
            "firstZone" to searchMarketParameters.firstZone,
            "name" to searchMarketParameters.name,
            "neighborhood" to searchMarketParameters.neighborhood
        )

        return criteria
            .filterValues { it?.isNotBlank() ?: false }
            .map { Criteria.where(it.key).isEqualTo(it.value) }
            .takeIf {
                it.isNotEmpty()
            }?.let {
                Criteria().andOperator(it)
            } ?: Criteria()
    }
}
