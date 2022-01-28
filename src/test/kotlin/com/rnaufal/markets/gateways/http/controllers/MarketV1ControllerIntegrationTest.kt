package com.rnaufal.markets.gateways.http.controllers

import com.rnaufal.markets.IntegrationTests
import com.rnaufal.markets.fixture.CreateMarketV1RequestFactory
import com.rnaufal.markets.fixture.MarketFactory
import com.rnaufal.markets.fixture.SearchMarketV1RequestFactory
import com.rnaufal.markets.fixture.UpdateMarketV1RequestFactory
import com.rnaufal.markets.gateways.repositories.MarketRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
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
            val createMarketV1Request = CreateMarketV1RequestFactory.buildSuccessfulRequest()

            webTestClient.post()
                .uri("/api/v1/markets")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(createMarketV1Request))
                .exchange()
                .expectStatus()
                .isCreated
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isNotEmpty
                .jsonPath("$.legacyIdentifier").isEqualTo(createMarketV1Request.legacyIdentifier)
                .jsonPath("$.longitude").isEqualTo(createMarketV1Request.longitude)
                .jsonPath("$.latitude").isEqualTo(createMarketV1Request.latitude)
                .jsonPath("$.setCens").isEqualTo(createMarketV1Request.setCens)
                .jsonPath("$.area").isEqualTo(createMarketV1Request.area)
                .jsonPath("$.districtCode").isEqualTo(createMarketV1Request.districtCode)
                .jsonPath("$.district").isEqualTo(createMarketV1Request.district)
                .jsonPath("$.townCode").isEqualTo(createMarketV1Request.townCode)
                .jsonPath("$.town").isEqualTo(createMarketV1Request.town)
                .jsonPath("$.firstZone").isEqualTo(createMarketV1Request.firstZone)
                .jsonPath("$.secondZone").isEqualTo(createMarketV1Request.secondZone)
                .jsonPath("$.name").isEqualTo(createMarketV1Request.name)
                .jsonPath("$.registryCode").isEqualTo(createMarketV1Request.registryCode)
                .jsonPath("$.publicArea").isEqualTo(createMarketV1Request.publicArea)
                .jsonPath("$.number").isEqualTo(createMarketV1Request.number!!)
                .jsonPath("$.neighborhood").isEqualTo(createMarketV1Request.neighborhood)
                .jsonPath("$.reference").isEmpty
        }

        @Test
        fun `should create market with reference successfully`(): Unit = runBlocking {
            val marketV1Request = CreateMarketV1RequestFactory.buildReferenceSuccessfulRequest()

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
                .jsonPath("$.legacyIdentifier").isEqualTo(marketV1Request.legacyIdentifier)
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
                .jsonPath("$.number").isEqualTo(marketV1Request.number!!)
                .jsonPath("$.neighborhood").isEqualTo(marketV1Request.neighborhood)
                .jsonPath("$.reference").isEqualTo(marketV1Request.reference!!)
        }

        @Test
        fun `should create market without number successfully`(): Unit = runBlocking {
            val createMarketV1Request = CreateMarketV1RequestFactory.buildReferenceSuccessfulRequestWithoutNumber()

            webTestClient.post()
                .uri("/api/v1/markets")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(createMarketV1Request))
                .exchange()
                .expectStatus()
                .isCreated
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isNotEmpty
                .jsonPath("$.legacyIdentifier").isEqualTo(createMarketV1Request.legacyIdentifier)
                .jsonPath("$.longitude").isEqualTo(createMarketV1Request.longitude)
                .jsonPath("$.latitude").isEqualTo(createMarketV1Request.latitude)
                .jsonPath("$.setCens").isEqualTo(createMarketV1Request.setCens)
                .jsonPath("$.area").isEqualTo(createMarketV1Request.area)
                .jsonPath("$.districtCode").isEqualTo(createMarketV1Request.districtCode)
                .jsonPath("$.district").isEqualTo(createMarketV1Request.district)
                .jsonPath("$.townCode").isEqualTo(createMarketV1Request.townCode)
                .jsonPath("$.town").isEqualTo(createMarketV1Request.town)
                .jsonPath("$.firstZone").isEqualTo(createMarketV1Request.firstZone)
                .jsonPath("$.secondZone").isEqualTo(createMarketV1Request.secondZone)
                .jsonPath("$.name").isEqualTo(createMarketV1Request.name)
                .jsonPath("$.registryCode").isEqualTo(createMarketV1Request.registryCode)
                .jsonPath("$.publicArea").isEqualTo(createMarketV1Request.publicArea)
                .jsonPath("$.number").isEmpty
                .jsonPath("$.neighborhood").isEqualTo(createMarketV1Request.neighborhood)
                .jsonPath("$.reference").isEqualTo(createMarketV1Request.reference!!)
        }

        @Test
        fun `should return bad request when market has validation errors`(): Unit = runBlocking {
            val marketV1Request = CreateMarketV1RequestFactory.buildInvalidRequest()

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
                        "legacyIdentifier",
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
            val market = MarketFactory.buildMarket()

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

    @Nested
    inner class FindMarketScenarios {

        @Test
        fun `should find market by id successfully`(): Unit = runBlocking {
            val market = MarketFactory.buildMarket()
                .run { marketRepository.save(this) }
                .awaitFirstOrElse { throw RuntimeException("Error saving market") }

            webTestClient.get()
                .uri("/api/v1/markets/${market?.id}")
                .exchange()
                .expectStatus()
                .isOk
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isNotEmpty
                .jsonPath("$.legacyIdentifier").isEqualTo(market.legacyIdentifier)
                .jsonPath("$.longitude").isEqualTo(market.longitude)
                .jsonPath("$.latitude").isEqualTo(market.latitude)
                .jsonPath("$.setCens").isEqualTo(market.setCens)
                .jsonPath("$.area").isEqualTo(market.area)
                .jsonPath("$.districtCode").isEqualTo(market.districtCode)
                .jsonPath("$.district").isEqualTo(market.district)
                .jsonPath("$.townCode").isEqualTo(market.townCode)
                .jsonPath("$.town").isEqualTo(market.town)
                .jsonPath("$.firstZone").isEqualTo(market.firstZone)
                .jsonPath("$.secondZone").isEqualTo(market.secondZone)
                .jsonPath("$.name").isEqualTo(market.name)
                .jsonPath("$.registryCode").isEqualTo(market.registryCode)
                .jsonPath("$.publicArea").isEqualTo(market.publicArea)
                .jsonPath("$.number").isEqualTo(market.number!!)
                .jsonPath("$.neighborhood").isEqualTo(market.neighborhood)
                .jsonPath("$.reference").isEqualTo(market.reference!!)
        }
    }

    @Nested
    inner class UpdateMarketScenarios {

        @Test
        fun `should update market successfully`(): Unit = runBlocking {
            val market = MarketFactory.buildMarket()
                .run { marketRepository.save(this) }
                .awaitFirstOrNull()

            val updateMarketV1Request = UpdateMarketV1RequestFactory.buildSuccessfulRequest()

            webTestClient.patch()
                .uri("/api/v1/markets/${market?.registryCode}")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(updateMarketV1Request))
                .exchange()
                .expectStatus()
                .isOk
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isNotEmpty
                .jsonPath("$.legacyIdentifier").isEqualTo(updateMarketV1Request.legacyIdentifier)
                .jsonPath("$.longitude").isEqualTo(updateMarketV1Request.longitude)
                .jsonPath("$.latitude").isEqualTo(updateMarketV1Request.latitude)
                .jsonPath("$.setCens").isEqualTo(updateMarketV1Request.setCens)
                .jsonPath("$.area").isEqualTo(updateMarketV1Request.area)
                .jsonPath("$.districtCode").isEqualTo(updateMarketV1Request.districtCode)
                .jsonPath("$.district").isEqualTo(updateMarketV1Request.district)
                .jsonPath("$.townCode").isEqualTo(updateMarketV1Request.townCode)
                .jsonPath("$.town").isEqualTo(updateMarketV1Request.town)
                .jsonPath("$.firstZone").isEqualTo(updateMarketV1Request.firstZone)
                .jsonPath("$.secondZone").isEqualTo(updateMarketV1Request.secondZone)
                .jsonPath("$.name").isEqualTo(updateMarketV1Request.name)
                .jsonPath("$.registryCode").isEqualTo(market?.registryCode!!)
                .jsonPath("$.publicArea").isEqualTo(updateMarketV1Request.publicArea)
                .jsonPath("$.number").isEqualTo(updateMarketV1Request.number!!)
                .jsonPath("$.neighborhood").isEqualTo(updateMarketV1Request.neighborhood)
                .jsonPath("$.reference").isEqualTo(updateMarketV1Request.reference!!)
        }

        @Test
        fun `should update market removing number and reference successfully`(): Unit = runBlocking {
            val market = MarketFactory.buildMarket()
                .run { marketRepository.save(this) }
                .awaitFirstOrNull()

            val updateMarketV1Request = UpdateMarketV1RequestFactory.buildSuccessfulRequestWithoutNumberAndReference()

            webTestClient.patch()
                .uri("/api/v1/markets/${market?.registryCode}")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(updateMarketV1Request))
                .exchange()
                .expectStatus()
                .isOk
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isNotEmpty
                .jsonPath("$.legacyIdentifier").isEqualTo(updateMarketV1Request.legacyIdentifier)
                .jsonPath("$.longitude").isEqualTo(updateMarketV1Request.longitude)
                .jsonPath("$.latitude").isEqualTo(updateMarketV1Request.latitude)
                .jsonPath("$.setCens").isEqualTo(updateMarketV1Request.setCens)
                .jsonPath("$.area").isEqualTo(updateMarketV1Request.area)
                .jsonPath("$.districtCode").isEqualTo(updateMarketV1Request.districtCode)
                .jsonPath("$.district").isEqualTo(updateMarketV1Request.district)
                .jsonPath("$.townCode").isEqualTo(updateMarketV1Request.townCode)
                .jsonPath("$.town").isEqualTo(updateMarketV1Request.town)
                .jsonPath("$.firstZone").isEqualTo(updateMarketV1Request.firstZone)
                .jsonPath("$.secondZone").isEqualTo(updateMarketV1Request.secondZone)
                .jsonPath("$.name").isEqualTo(updateMarketV1Request.name)
                .jsonPath("$.registryCode").isEqualTo(market?.registryCode!!)
                .jsonPath("$.publicArea").isEqualTo(updateMarketV1Request.publicArea)
                .jsonPath("$.number").isEmpty
                .jsonPath("$.neighborhood").isEqualTo(updateMarketV1Request.neighborhood)
                .jsonPath("$.reference").isEmpty
        }

        @Test
        fun `should return error response when updating market not found`(): Unit = runBlocking {
            val updateMarketV1Request = UpdateMarketV1RequestFactory.buildSuccessfulRequest()

            webTestClient.patch()
                .uri("/api/v1/markets/1234-5")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(updateMarketV1Request))
                .exchange()
                .expectStatus()
                .isNotFound
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.message").isNotEmpty
        }
    }

    @Nested
    inner class SearchMarketScenarios {

        @Test
        fun `should search market by all criteria successfully`(): Unit = runBlocking {
            val market = MarketFactory.buildMarket()
                .run { marketRepository.save(this) }
                .awaitFirstOrElse { throw RuntimeException("Error saving market") }

            val searchMarketV1Request = SearchMarketV1RequestFactory.buildCompleteSearchRequest()

            val uri = """
                |/api/v1/markets?district=${searchMarketV1Request.district}
                |&firstZone=${searchMarketV1Request.firstZone}
                |&name=${searchMarketV1Request.name}
                |&neighborhood=${searchMarketV1Request.neighborhood}
            """.trimMargin().replace("\n", "")

            webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.content[0].id").isNotEmpty
                .jsonPath("$.content[0].legacyIdentifier").isEqualTo(market.legacyIdentifier)
                .jsonPath("$.content[0].longitude").isEqualTo(market.longitude)
                .jsonPath("$.content[0].latitude").isEqualTo(market.latitude)
                .jsonPath("$.content[0].setCens").isEqualTo(market.setCens)
                .jsonPath("$.content[0].area").isEqualTo(market.area)
                .jsonPath("$.content[0].districtCode").isEqualTo(market.districtCode)
                .jsonPath("$.content[0].district").isEqualTo(market.district)
                .jsonPath("$.content[0].townCode").isEqualTo(market.townCode)
                .jsonPath("$.content[0].town").isEqualTo(market.town)
                .jsonPath("$.content[0].firstZone").isEqualTo(market.firstZone)
                .jsonPath("$.content[0].secondZone").isEqualTo(market.secondZone)
                .jsonPath("$.content[0].name").isEqualTo(market.name)
                .jsonPath("$.content[0].registryCode").isEqualTo(market.registryCode)
                .jsonPath("$.content[0].publicArea").isEqualTo(market.publicArea)
                .jsonPath("$.content[0].number").isEqualTo(market.number!!)
                .jsonPath("$.content[0].neighborhood").isEqualTo(market.neighborhood)
                .jsonPath("$.content[0].reference").isEqualTo(market.reference!!)
                .jsonPath("$.totalPages").isEqualTo(1)
                .jsonPath("$.totalElements").isEqualTo(1)
                .jsonPath("$.numberOfElements").isEqualTo(1)
        }

        @Test
        fun `should search market by name criteria with pagination`(): Unit = runBlocking {
            val firstMarket = MarketFactory.buildMarket()
            val secondMarket = MarketFactory.buildUpdatedMarketChangingRegistryCode()
            marketRepository.saveAll(listOf(firstMarket, secondMarket)).awaitFirstOrNull()

            val searchMarketV1Request = SearchMarketV1RequestFactory.buildCompleteSearchRequest()

            webTestClient.get()
                .uri("/api/v1/markets?firstZone=${searchMarketV1Request.firstZone}&page=2&size=1")
                .exchange()
                .expectStatus()
                .isOk
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.content[0].id").isNotEmpty
                .jsonPath("$.content[0].legacyIdentifier").isEqualTo(secondMarket.legacyIdentifier)
                .jsonPath("$.content[0].longitude").isEqualTo(secondMarket.longitude)
                .jsonPath("$.content[0].latitude").isEqualTo(secondMarket.latitude)
                .jsonPath("$.content[0].setCens").isEqualTo(secondMarket.setCens)
                .jsonPath("$.content[0].area").isEqualTo(secondMarket.area)
                .jsonPath("$.content[0].districtCode").isEqualTo(secondMarket.districtCode)
                .jsonPath("$.content[0].district").isEqualTo(secondMarket.district)
                .jsonPath("$.content[0].townCode").isEqualTo(secondMarket.townCode)
                .jsonPath("$.content[0].town").isEqualTo(secondMarket.town)
                .jsonPath("$.content[0].firstZone").isEqualTo(secondMarket.firstZone)
                .jsonPath("$.content[0].secondZone").isEqualTo(secondMarket.secondZone)
                .jsonPath("$.content[0].name").isEqualTo(secondMarket.name)
                .jsonPath("$.content[0].registryCode").isEqualTo(secondMarket.registryCode)
                .jsonPath("$.content[0].publicArea").isEqualTo(secondMarket.publicArea)
                .jsonPath("$.content[0].number").isEqualTo(secondMarket.number!!)
                .jsonPath("$.content[0].neighborhood").isEqualTo(secondMarket.neighborhood)
                .jsonPath("$.content[0].reference").isEqualTo(secondMarket.reference!!)
                .jsonPath("$.totalPages").isEqualTo(2)
                .jsonPath("$.totalElements").isEqualTo(2)
                .jsonPath("$.numberOfElements").isEqualTo(1)
        }

        @Test
        fun `should return no search result when there are no markets`(): Unit = runBlocking {
            val searchMarketV1Request = SearchMarketV1RequestFactory.buildCompleteSearchRequest()

            webTestClient.get()
                .uri("/api/v1/markets?firstZone=${searchMarketV1Request.firstZone}&page=2&size=1")
                .exchange()
                .expectStatus()
                .isOk
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.content.length()").isEqualTo(0)
                .jsonPath("$.totalPages").isEqualTo(0)
                .jsonPath("$.totalElements").isEqualTo(0)
                .jsonPath("$.numberOfElements").isEqualTo(0)
        }

        @Test
        fun `should search all markets`(): Unit = runBlocking {
            val market = MarketFactory.buildUpdatedMarketWithSameRegistryCode()
                .run { marketRepository.save(this) }
                .awaitFirstOrElse { throw RuntimeException("Error saving market") }

            webTestClient.get()
                .uri("/api/v1/markets")
                .exchange()
                .expectStatus()
                .isOk
                .expectHeader().valueEquals("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.content[0].id").isNotEmpty
                .jsonPath("$.content[0].legacyIdentifier").isEqualTo(market.legacyIdentifier)
                .jsonPath("$.content[0].longitude").isEqualTo(market.longitude)
                .jsonPath("$.content[0].latitude").isEqualTo(market.latitude)
                .jsonPath("$.content[0].setCens").isEqualTo(market.setCens)
                .jsonPath("$.content[0].area").isEqualTo(market.area)
                .jsonPath("$.content[0].districtCode").isEqualTo(market.districtCode)
                .jsonPath("$.content[0].district").isEqualTo(market.district)
                .jsonPath("$.content[0].townCode").isEqualTo(market.townCode)
                .jsonPath("$.content[0].town").isEqualTo(market.town)
                .jsonPath("$.content[0].firstZone").isEqualTo(market.firstZone)
                .jsonPath("$.content[0].secondZone").isEqualTo(market.secondZone)
                .jsonPath("$.content[0].name").isEqualTo(market.name)
                .jsonPath("$.content[0].registryCode").isEqualTo(market.registryCode)
                .jsonPath("$.content[0].publicArea").isEqualTo(market.publicArea)
                .jsonPath("$.content[0].number").isEqualTo(market.number!!)
                .jsonPath("$.content[0].neighborhood").isEqualTo(market.neighborhood)
                .jsonPath("$.content[0].reference").isEqualTo(market.reference!!)
                .jsonPath("$.totalPages").isEqualTo(1)
                .jsonPath("$.totalElements").isEqualTo(1)
                .jsonPath("$.numberOfElements").isEqualTo(1)
        }
    }
}
