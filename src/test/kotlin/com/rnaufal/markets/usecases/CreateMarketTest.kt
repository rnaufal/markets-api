package com.rnaufal.markets.usecases

import com.rnaufal.markets.fixture.MarketFixtureFactory
import com.rnaufal.markets.gateways.MarketGateway
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CreateMarketTest(@MockK private val marketGateway: MarketGateway) {

    private val createMarket = CreateMarket(marketGateway)

    @Test
    fun `should create new market with success`(): Unit = runBlocking {
        val market = MarketFixtureFactory.buildMarket()

        coEvery { marketGateway.findByRegistryCode(market.registryCode) } returns null
        coEvery { marketGateway.save(market) } returns market

        val created = createMarket.execute(market)
        assertThat(created).usingRecursiveComparison().isEqualTo(market)

        coVerify { marketGateway.findByRegistryCode(market.registryCode) }
        coVerify { marketGateway.save(market) }
        confirmVerified(marketGateway)
    }

    @Test
    fun `should return existing market`(): Unit = runBlocking {
        val expected = MarketFixtureFactory.buildMarket()

        coEvery { marketGateway.findByRegistryCode(expected.registryCode) } returns expected

        val created = createMarket.execute(expected)
        assertThat(created).usingRecursiveComparison().isEqualTo(expected)

        coVerify { marketGateway.findByRegistryCode(expected.registryCode) }
        confirmVerified(marketGateway)
    }
}
