package com.rnaufal.markets.config.data

import com.rnaufal.markets.domains.Market
import com.rnaufal.markets.gateways.MarketGateway
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
    private val marketGateway: MarketGateway,
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
        val csvParser = buildCsvParser(marketsDataResource)
        for (csvRecord in csvParser) {
            val identifier = csvRecord.get("ID").toInt()
            val longitude = csvRecord.get("LONG").toLong()
            val latitude = csvRecord.get("LAT").toLong()
            val setCens = csvRecord.get("SETCENS").toLong()
            val area = csvRecord.get("AREAP").toLong()
            val districtCode = csvRecord.get("CODDIST").toInt()
            val district = csvRecord.get("DISTRITO").toString()
            val townCode = csvRecord.get("CODSUBPREF").toInt()
            val town = csvRecord.get("SUBPREFE").toString()
            val firstZone = csvRecord.get("REGIAO5").toString()
            val secondZone = csvRecord.get("REGIAO8").toString()
            val marketName = csvRecord.get("NOME_FEIRA").toString()
            val registryCode = csvRecord.get("REGISTRO").toString()
            val publicArea = csvRecord.get("LOGRADOURO").toString()
            val number = csvRecord.get("NUMERO").toString()
            val neighborhood = csvRecord.get("BAIRRO").toString()
            val reference = when {
                csvRecord.isSet("REFERENCIA") -> csvRecord.get("REFERENCIA").toString()
                else -> null
            }
            runBlocking {
                val market = Market(
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
                marketGateway.findByRegistryCode(market.registryCode)?.also {
                    logger.info { "$it already loaded successfully" }
                } ?: marketGateway.save(market).also {
                    logger.info { "$it saved successfully" }
                }
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
