package com.rnaufal.markets.fixture

import com.rnaufal.markets.gateways.http.json.SearchMarketV1Request

class SearchMarketV1RequestFactory {

    companion object {

        fun buildCompleteSearchRequest() =
            SearchMarketV1Request(
                district = "VILA FORMOSA",
                firstZone = "Leste",
                name = "VILA FORMOSA",
                neighborhood = "VL FORMOSA",
            )
    }
}
