package com.rnaufal.markets.fixture

import com.rnaufal.markets.gateways.http.json.UpdateMarketV1Request

class UpdateMarketV1RequestFactory {

    companion object {
        fun buildSuccessfulRequest() =
            buildSuccessfulRequestWithoutNumberAndReference().copy(
                number = "417.000000",
                reference = "TV AV DEP EMILIO CARLOS"
            )

        fun buildSuccessfulRequestWithoutNumberAndReference() =
            UpdateMarketV1Request(
                legacyIdentifier = 16,
                longitude = -46674080,
                latitude = -23486660,
                setCens = 355030850000100,
                area = 3550308005060,
                districtCode = 50,
                district = "LIMAO",
                townCode = 4,
                town = "CASA VERDE-CACHOEIRINHA",
                firstZone = "Norte",
                secondZone = "Norte 1",
                name = "SANTA MARIA",
                publicArea = "RUA TOMAZ ANTONIO VILANE",
                neighborhood = "VL STA MARIA",
            )
    }
}
