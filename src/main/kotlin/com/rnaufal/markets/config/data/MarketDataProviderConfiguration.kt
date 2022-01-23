package com.rnaufal.markets.config.data

import com.rnaufal.markets.domains.Market
import com.rnaufal.markets.gateways.repositories.MarketRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import java.io.InputStreamReader

@Configuration
class MarketDataProviderConfiguration(
    private val marketRepository: MarketRepository,
    @Value("classpath:DEINFO_AB_FEIRASLIVRES_2014.csv") private val marketsDataResource: Resource
) {

    companion object {
        private val logger = KotlinLogging.logger { }
    }

    @Bean
    @Profile("!test")
    fun loadMarkets():
        ApplicationListener<ApplicationReadyEvent> {
        return ApplicationListener {
            internalLoadMarkets()
        }
    }

    internal fun internalLoadMarkets() {
        runBlocking {
            logger.info { "Deleting all markets from database" }

            marketRepository.deleteAll().awaitFirstOrNull()

            val csvParser = buildCsvParser(marketsDataResource)
            val markets = csvParser.records
                .map {
                    val identifier = it["ID"].toInt()
                    val longitude = it["LONG"].toLong()
                    val latitude = it["LAT"].toLong()
                    val setCens = it["SETCENS"].toLong()
                    val area = it["AREAP"].toLong()
                    val districtCode = it["CODDIST"].toInt()
                    val district = it["DISTRITO"].toString()
                    val townCode = it["CODSUBPREF"].toInt()
                    val town = it["SUBPREFE"].toString()
                    val firstZone = it["REGIAO5"].toString()
                    val secondZone = it["REGIAO8"].toString()
                    val marketName = it["NOME_FEIRA"].toString()
                    val registryCode = it["REGISTRO"].toString()
                    val publicArea = it["LOGRADOURO"].toString()
                    val number = it["NUMERO"].toString()
                    val neighborhood = it["BAIRRO"].toString()
                    val reference = when {
                        it.isSet("REFERENCIA") -> it["REFERENCIA"].toString()
                        else -> null
                    }
                    Market(
                        identifier = identifier,
                        longitude = longitude,
                        latitude = latitude,
                        setCens = setCens,
                        area = area,
                        districtCode = districtCode,
                        district = district,
                        townCode = townCode,
                        town = town,
                        firstZone = firstZone,
                        secondZone = secondZone,
                        name = marketName,
                        registryCode = registryCode,
                        publicArea = publicArea,
                        number = number,
                        neighborhood = neighborhood,
                        reference = reference
                    )
                }

            marketRepository.saveAll(markets).awaitFirstOrNull().also {
                logger.info { "Successfully loaded ${markets.size} markets into database" }
            }
        }
    }

    private fun buildCsvParser(
        marketsDataResource: Resource
    ) = CSVParser(InputStreamReader(marketsDataResource.inputStream), buildCsvFormat())

    private fun buildCsvFormat() = CSVFormat
        .Builder
        .create()
        .apply {
            setHeader()
            setSkipHeaderRecord(true)
            setIgnoreHeaderCase(true)
            setTrim(true)
        }
        .build()
}
