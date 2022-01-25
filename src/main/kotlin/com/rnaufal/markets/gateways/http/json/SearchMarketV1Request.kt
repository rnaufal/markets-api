package com.rnaufal.markets.gateways.http.json

import com.rnaufal.markets.domains.SearchMarketParameters

data class SearchMarketV1Request(
    val district: String? = null,
    val firstZone: String? = null,
    val name: String? = null,
    val neighborhood: String? = null,
)

fun SearchMarketV1Request.toSearchParameters() = SearchMarketParameters(
    district = district,
    firstZone = firstZone,
    name = name,
    neighborhood = neighborhood
)
