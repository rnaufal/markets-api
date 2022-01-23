package com.rnaufal.markets.config.data

import com.rnaufal.markets.IntegrationTests
import com.rnaufal.markets.gateways.repositories.MarketRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class MarketDataProviderConfigurationIntegrationTest(
    @Autowired private val marketDataProviderConfiguration: MarketDataProviderConfiguration,
    @Autowired private val marketRepository: MarketRepository,
) : IntegrationTests(marketRepository) {

    @Test
    fun `should load market data successfully`(): Unit = runBlocking {
        marketDataProviderConfiguration.internalLoadMarkets()

        assertThat(marketRepository.count().awaitFirstOrNull()).isNotZero()
    }
}
