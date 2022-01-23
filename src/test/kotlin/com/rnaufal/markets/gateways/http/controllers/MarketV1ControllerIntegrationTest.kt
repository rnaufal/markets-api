package com.rnaufal.markets.gateways.http.controllers

import com.rnaufal.markets.IntegrationTests
import com.rnaufal.markets.fixture.MarketFixtureFactory
import com.rnaufal.markets.fixture.MarketV1RequestFixtureFactory
import com.rnaufal.markets.gateways.repositories.MarketRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

class MarketV1ControllerIntegrationTest(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val marketRepository: MarketRepository,
) : IntegrationTests(marketRepository) {

    @Nested
    inner class CreateMarketScenarios {

        @Test
        fun `should create market without reference successfully`(): Unit = runBlocking {
            val marketV1Request = MarketV1RequestFixtureFactory.buildSuccessfulRequest()

            webTestClient.post()
                .uri("/api/v1/markets")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(marketV1Request))
                .exchange()
                .expectStatus()
                .isCreated
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isNotEmpty
                .jsonPath("$.identifier").isEqualTo(marketV1Request.identifier)
                .jsonPath("$.longitude").isEqualTo(marketV1Request.longitude)
                .jsonPath("$.latitude").isEqualTo(marketV1Request.latitude)
                .jsonPath("$.setCens").isEqualTo(marketV1Request.setCens)
                .jsonPath("$.area").isEqualTo(marketV1Request.area)
                .jsonPath("$.districtCode").isEqualTo(marketV1Request.districtCode)
                .jsonPath("$.district").isEqualTo(marketV1Request.district)
                .jsonPath("$.townCode").isEqualTo(marketV1Request.townCode)
                .jsonPath("$.town").isEqualTo(marketV1Request.town)
                .jsonPath("$.firstZone").isEqualTo(marketV1Request.firstZone)
                .jsonPath("$.secondZone").isEqualTo(marketV1Request.secondZone)
                .jsonPath("$.name").isEqualTo(marketV1Request.name)
                .jsonPath("$.registryCode").isEqualTo(marketV1Request.registryCode)
                .jsonPath("$.publicArea").isEqualTo(marketV1Request.publicArea)
                .jsonPath("$.number").isEqualTo(marketV1Request.number)
                .jsonPath("$.neighborhood").isEqualTo(marketV1Request.neighborhood)
                .jsonPath("$.reference").isEmpty
        }

        @Test
        fun `should create market with reference successfully`(): Unit = runBlocking {
            val marketV1Request = MarketV1RequestFixtureFactory.buildReferenceSuccessfulRequest()

            webTestClient.post()
                .uri("/api/v1/markets")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(marketV1Request))
                .exchange()
                .expectStatus()
                .isCreated
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isNotEmpty
                .jsonPath("$.identifier").isEqualTo(marketV1Request.identifier)
                .jsonPath("$.longitude").isEqualTo(marketV1Request.longitude)
                .jsonPath("$.latitude").isEqualTo(marketV1Request.latitude)
                .jsonPath("$.setCens").isEqualTo(marketV1Request.setCens)
                .jsonPath("$.area").isEqualTo(marketV1Request.area)
                .jsonPath("$.districtCode").isEqualTo(marketV1Request.districtCode)
                .jsonPath("$.district").isEqualTo(marketV1Request.district)
                .jsonPath("$.townCode").isEqualTo(marketV1Request.townCode)
                .jsonPath("$.town").isEqualTo(marketV1Request.town)
                .jsonPath("$.firstZone").isEqualTo(marketV1Request.firstZone)
                .jsonPath("$.secondZone").isEqualTo(marketV1Request.secondZone)
                .jsonPath("$.name").isEqualTo(marketV1Request.name)
                .jsonPath("$.registryCode").isEqualTo(marketV1Request.registryCode)
                .jsonPath("$.publicArea").isEqualTo(marketV1Request.publicArea)
                .jsonPath("$.number").isEqualTo(marketV1Request.number)
                .jsonPath("$.neighborhood").isEqualTo(marketV1Request.neighborhood)
                .jsonPath("$.reference").isEqualTo(marketV1Request.reference!!)
        }

        @Test
        fun `should return bad request when market has validation errors`(): Unit = runBlocking {
            val marketV1Request = MarketV1RequestFixtureFactory.buildInvalidRequest()

            webTestClient.post()
                .uri("/api/v1/markets")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(marketV1Request))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.errors.length()").isEqualTo(4)
                .jsonPath("$.errors[*].property").value(
                    containsInAnyOrder(
                        "identifier",
                        "area",
                        "district",
                        "registryCode",
                    )
                ).jsonPath("$.errors[*].value").value(
                    containsInAnyOrder(
                        "-35",
                        "-50",
                        "",
                        "",
                    )
                )
                .jsonPath("$.errors[*].errorMessage").isNotEmpty
        }
    }

    @Nested
    inner class DeleteMarketScenarios {

        @Test
        fun `should delete market successfully`(): Unit = runBlocking {
            val market = MarketFixtureFactory.buildMarket()

            marketRepository.save(market).awaitFirstOrNull()

            webTestClient.delete()
                .uri("/api/v1/markets/${market.registryCode}")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent
        }

        @Test
        fun `should return error response when deleting market not found`(): Unit = runBlocking {
            webTestClient.delete()
                .uri("/api/v1/markets/3456-2")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound
                .expectBody()
                .jsonPath("$.message").isNotEmpty
        }
    }
}
