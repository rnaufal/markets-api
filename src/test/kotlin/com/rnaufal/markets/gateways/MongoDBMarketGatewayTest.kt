package com.rnaufal.markets.gateways

import com.rnaufal.markets.domains.Market
import com.rnaufal.markets.fixture.MarketFactory
import com.rnaufal.markets.fixture.SearchParametersFactory
import com.rnaufal.markets.gateways.repositories.MarketRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import reactor.core.publisher.Mono

@ExtendWith(MockKExtension::class)
class MongoDBMarketGatewayTest(@MockK private val marketRepository: MarketRepository) {

    private val marketGateway = MongoDBMarketGateway(marketRepository)

    @Test
    fun `should save market successfully`() = runBlocking {
        val market = MarketFactory.buildMarket()

        val marketCreationSlot = slot<Market>()
        coEvery { marketRepository.save(capture(marketCreationSlot)) } returns Mono.just(market)

        val created = marketGateway.save(market)

        val capturedMarket = marketCreationSlot.captured
        assertThat(created).usingRecursiveComparison().isEqualTo(capturedMarket)

        verify { marketRepository.save(market) }
        confirmVerified(marketRepository)
    }

    @Test
    fun `should find market by code successfully`() = runBlocking {
        val market = MarketFactory.buildMarket()

        coEvery { marketRepository.findByRegistryCode(market.registryCode) } returns Mono.just(market)

        val maybeMarket = marketGateway.findByRegistryCode(market.registryCode)

        assertThat(maybeMarket).usingRecursiveComparison().isEqualTo(market)

        verify { marketRepository.findByRegistryCode(market.registryCode) }
        confirmVerified(marketRepository)
    }

    @Test
    fun `should delete market successfully`() = runBlocking {
        val market = MarketFactory.buildMarket()

        val marketDeletionSlot = slot<Market>()
        coEvery { marketRepository.delete(capture(marketDeletionSlot)) } returns Mono.empty()

        marketGateway.delete(market)

        val capturedMarket = marketDeletionSlot.captured
        assertThat(capturedMarket).usingRecursiveComparison().isEqualTo(market)

        verify { marketRepository.delete(market) }
        confirmVerified(marketRepository)
    }

    @Test
    fun `should find market by id successfully`() = runBlocking {
        val market = MarketFactory.buildMarketWithId()

        coEvery { marketRepository.findByRegistryCode(market.id.toString()) } returns Mono.just(market)

        val maybeMarket = marketGateway.findByRegistryCode(market.id.toString())

        assertThat(maybeMarket).usingRecursiveComparison().isEqualTo(market)

        verify { marketRepository.findByRegistryCode(market.id.toString()) }
        confirmVerified(marketRepository)
    }

    @Test
    fun `should search market by criteria`() = runBlocking {
        val market = MarketFactory.buildMarketWithId()
        val searchMarketParameters = SearchParametersFactory.buildSearchParametersWithOnlyName()
        val pageable = PageRequest.of(0, 1)

        coEvery {
            marketRepository.searchByCriteria(
                searchMarketParameters,
                pageable
            )
        } returns PageImpl(listOf(market))

        val pageResults = marketGateway.search(
            searchMarketParameters,
            pageable
        )

        assertThat(pageResults.totalElements).isEqualTo(1)
        assertThat(pageResults.totalPages).isEqualTo(1)
        assertThat(pageResults.numberOfElements).isEqualTo(1)
        assertThat(pageResults.size).isEqualTo(1)
        assertThat(pageResults.content.size).isEqualTo(1)
        assertThat(pageResults.content[0]).usingRecursiveComparison().isEqualTo(market)

        coVerify {
            marketRepository.searchByCriteria(
                searchMarketParameters,
                pageable
            )
        }
        confirmVerified(marketRepository)
    }
}
