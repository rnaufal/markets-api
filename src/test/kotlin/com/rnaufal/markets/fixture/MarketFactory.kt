package com.rnaufal.markets.fixture

import com.rnaufal.markets.domains.Market
import java.util.UUID

class MarketFactory {

    companion object {

        fun buildMarket() =
            Market(
                legacyIdentifier = 1,
                longitude = -46550164,
                latitude = -23558733,
                setCens = 355030885000091,
                area = 3550308005040,
                districtCode = 87,
                district = "VILA FORMOSA",
                townCode = 26,
                town = "ARICANDUVA-FORMOSA-CARRAO",
                firstZone = "Leste",
                secondZone = "Leste 1",
                name = "VILA FORMOSA",
                registryCode = "4041-0",
                publicArea = "RUA MARAGOJIPE",
                number = "S/N",
                neighborhood = "VL FORMOSA",
                reference = "TV RUA PRETORIA"
            )

        fun buildMarketWithId() =
            buildMarket().copy(id = UUID.randomUUID().toString())

        fun buildUpdatedMarketWithSameRegistryCode() = buildMarket().copy(
            legacyIdentifier = 4,
            longitude = -46513450,
            latitude = -23520880,
            setCens = 355030859000173,
            area = 3550308005145,
            districtCode = 60,
            district = "PENHA",
            townCode = 21,
            town = "PENHA",
            firstZone = "Leste",
            secondZone = "Leste 1",
            name = "VILA NOVA GRANADA",
            publicArea = "RUA FRANCISCO DE OLIVEIRA BRAGA",
            number = "13.000000",
            neighborhood = "VL NOVA GRANADA",
            reference = "RUA OLIVIA DE OLIVEIRA"
        )

        fun buildUpdatedMarketChangingRegistryCode() = buildUpdatedMarketWithSameRegistryCode().copy(
            registryCode = "4567-2"
        )
    }
}
