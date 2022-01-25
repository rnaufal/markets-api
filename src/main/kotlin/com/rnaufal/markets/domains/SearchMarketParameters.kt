package com.rnaufal.markets.domains

data class SearchMarketParameters(
    val district: String? = null,
    val firstZone: String? = null,
    val name: String? = null,
    val neighborhood: String? = null,
)
