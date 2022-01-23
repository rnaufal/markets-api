package com.rnaufal.markets.fixture

import com.rnaufal.markets.gateways.http.json.CreateMarketV1Request

class MarketV1RequestFixtureFactory {

    companion object {
        fun buildSuccessfulRequest() =
            CreateMarketV1Request(
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
                neighborhood = "VL FORMOSA"
            )

        fun buildReferenceSuccessfulRequest(): CreateMarketV1Request {
            return buildSuccessfulRequest().copy(reference = "TV RUA PRETORIA")
        }

        fun buildInvalidRequest(): CreateMarketV1Request {
            return buildSuccessfulRequest().copy(
                legacyIdentifier = -35,
                area = -50,
                district = "",
                registryCode = "",
            )
        }
    }
}
