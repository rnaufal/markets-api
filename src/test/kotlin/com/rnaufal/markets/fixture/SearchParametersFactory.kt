package com.rnaufal.markets.fixture

import com.rnaufal.markets.domains.SearchMarketParameters

class SearchParametersFactory {

    companion object {

        fun buildSearchParametersWithoutFirstZone() =
            buildCompleteSearchParameters().copy(firstZone = null)

        fun buildSearchParametersWithOnlyName() =
            buildCompleteSearchParameters().copy(
                district = null,
                firstZone = null,
                neighborhood = null
            )

        private fun buildCompleteSearchParameters() =
            SearchMarketParameters(
                district = "<<DISTRICT>>",
                firstZone = "<<FIRST_ZONE>>",
                name = "<<NAME>>",
                neighborhood = "<<NEIGHBORHOOD>>",
            )
    }
}
