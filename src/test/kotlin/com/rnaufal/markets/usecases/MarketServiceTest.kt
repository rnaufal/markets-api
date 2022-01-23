package com.rnaufal.markets.usecases

import com.rnaufal.markets.domains.Market
import com.rnaufal.markets.exceptions.MarketNotFoundException
import com.rnaufal.markets.fixture.MarketFixtureFactory
import com.rnaufal.markets.gateways.MarketGateway
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MarketServiceTest(@MockK private val marketGateway: MarketGateway) {

    private val marketService = MarketService(marketGateway)

    @Nested
    inner class CreateMarketTests {

        @Test
        fun `should create new market with success`() = runBlocking {
            val market = MarketFixtureFactory.buildMarket()

            coEvery { marketGateway.findByRegistryCode(market.registryCode) } returns null
            coEvery { marketGateway.save(market) } returns market

            val created = marketService.execute(market)
            assertThat(created).usingRecursiveComparison().isEqualTo(market)

            coVerify { marketGateway.findByRegistryCode(market.registryCode) }
            coVerify { marketGateway.save(market) }
            confirmVerified(marketGateway)
        }

        @Test
        fun `should return existing market`() = runBlocking {
            val expected = MarketFixtureFactory.buildMarket()

            coEvery { marketGateway.findByRegistryCode(expected.registryCode) } returns expected

            val created = marketService.execute(expected)
            assertThat(created).usingRecursiveComparison().isEqualTo(expected)

            coVerify { marketGateway.findByRegistryCode(expected.registryCode) }
            coVerify(exactly = 0) { marketGateway.save(expected) }
            confirmVerified(marketGateway)
        }
    }

    @Nested
    inner class DeleteMarketTests {

        @Test
        fun `should delete existing market with success`() = runBlocking {
            val market = MarketFixtureFactory.buildMarket()

            val marketDeletionSlot = slot<Market>()
            coEvery { marketGateway.findByRegistryCode(market.registryCode) } returns market
            coEvery { marketGateway.delete(capture(marketDeletionSlot)) } returns Unit

            marketService.delete(market.registryCode)

            val capturedMarket = marketDeletionSlot.captured
            assertThat(capturedMarket).usingRecursiveComparison().isEqualTo(market)

            coVerify { marketGateway.findByRegistryCode(market.registryCode) }
            coVerify { marketGateway.delete(market) }
            confirmVerified(marketGateway)
        }

        @Test
        fun `should throw exception when trying to delete a market not found by code`() = runBlocking {
            val market = MarketFixtureFactory.buildMarket()

            coEvery { marketGateway.findByRegistryCode(market.registryCode) } returns null

            assertThrows<MarketNotFoundException> { marketService.delete(market.registryCode) }

            coVerify { marketGateway.findByRegistryCode(market.registryCode) }
            coVerify(exactly = 0) { marketGateway.delete(market) }
            confirmVerified(marketGateway)
        }
    }

    @Nested
    inner class FindMarketTests {

        @Test
        fun `should find market by id successfully`() = runBlocking {
            val market = MarketFixtureFactory.buildMarketWithId()

            coEvery { marketGateway.findById(market.id.toString()) } returns market

            val marketFound = marketService.getById(market.id.toString())

            assertThat(marketFound).usingRecursiveComparison().isEqualTo(market)

            coVerify { marketGateway.findById(market.id.toString()) }
            confirmVerified(marketGateway)
        }

        @Test
        fun `should throw exception when market not found by id`() = runBlocking {
            val id = "123456"

            coEvery { marketGateway.findById(id) } returns null

            assertThrows<MarketNotFoundException> { marketService.getById(id) }

            coVerify { marketGateway.findById(id) }
            confirmVerified(marketGateway)
        }
    }
}
