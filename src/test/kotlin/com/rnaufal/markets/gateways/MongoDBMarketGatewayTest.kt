package com.rnaufal.markets.gateways

import com.rnaufal.markets.domains.Market
import com.rnaufal.markets.fixture.MarketFixtureFactory
import com.rnaufal.markets.gateways.repositories.MarketRepository
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import reactor.core.publisher.Mono

@ExtendWith(MockKExtension::class)
class MongoDBMarketGatewayTest(@MockK private val marketRepository: MarketRepository) {

    private val marketGateway = MongoDBMarketGateway(marketRepository)

    @Test
    fun `should save market successfully`() = runBlocking {
        val market = MarketFixtureFactory.buildMarket()

        val marketCreationSlot = slot<Market>()
        coEvery { marketRepository.save(capture(marketCreationSlot)) } returns Mono.just(market)

        val created = marketGateway.save(market)

        val capturedMarket = marketCreationSlot.captured
        assertThat(created).usingRecursiveComparison().isEqualTo(capturedMarket)

        verify { marketRepository.save(market) }
        confirmVerified(marketRepository)
    }

    @Test
    fun `should find market successfully`() = runBlocking {
        val market = MarketFixtureFactory.buildMarket()

        coEvery { marketRepository.findByRegistryCode(market.registryCode) } returns Mono.just(market)

        val maybeMarket = marketGateway.findByRegistryCode(market.registryCode)

        assertThat(maybeMarket).usingRecursiveComparison().isEqualTo(market)

        verify { marketRepository.findByRegistryCode(market.registryCode) }
        confirmVerified(marketRepository)
    }

    @Test
    fun `should delete market successfully`() = runBlocking {
        val market = MarketFixtureFactory.buildMarket()

        val marketDeletionSlot = slot<Market>()
        coEvery { marketRepository.delete(capture(marketDeletionSlot)) } returns Mono.empty()

        marketGateway.delete(market)

        val capturedMarket = marketDeletionSlot.captured
        assertThat(capturedMarket).usingRecursiveComparison().isEqualTo(market)

        verify { marketRepository.delete(market) }
        confirmVerified(marketRepository)
    }
}
