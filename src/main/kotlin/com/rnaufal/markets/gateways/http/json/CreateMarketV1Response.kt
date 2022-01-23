package com.rnaufal.markets.gateways.http.json

import com.rnaufal.markets.domains.Market

data class CreateMarketV1Response(
    val id: String?,
    val legacyIdentifier: Int,
    val longitude: Long,
    val latitude: Long,
    val setCens: Long,
    val area: Long,
    val districtCode: Int,
    val district: String,
    val townCode: Int,
    val town: String,
    val firstZone: String,
    val secondZone: String,
    val name: String,
    val registryCode: String,
    val publicArea: String,
    val number: String,
    val neighborhood: String,
    val reference: String? = null,
)

fun Market.toResponse(): CreateMarketV1Response {
    return CreateMarketV1Response(
        id = id,
        legacyIdentifier = legacyIdentifier,
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
        name = name,
        registryCode = registryCode,
        publicArea = publicArea,
        number = number,
        neighborhood = neighborhood,
        reference = reference,
    )
}
