package com.rnaufal.markets

import com.rnaufal.markets.gateways.repositories.MarketRepository
import com.rnaufal.markets.testcontainers.MongoContainer
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
@ActiveProfiles("test")
class IntegrationTests(private val marketRepository: MarketRepository) {

    @BeforeEach
    fun setUp() {
        runBlocking {
            marketRepository.deleteAll().awaitFirstOrNull()
        }
    }

    companion object {

        @Container
        private val mongoContainer = MongoContainer

        @JvmStatic
        @DynamicPropertySource
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            mongoContainer.configure(registry)
        }
    }
}
