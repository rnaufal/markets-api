package com.rnaufal.markets.gateways.http.json

import com.rnaufal.markets.domains.Market
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isPositive
import org.valiktor.validate

data class UpdateMarketV1Request(
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
    val publicArea: String,
    val number: String,
    val neighborhood: String,
    val reference: String? = null,
)

fun UpdateMarketV1Request.validate() {
    validate(this) {
        validate(UpdateMarketV1Request::legacyIdentifier).isGreaterThan(0)
        validate(UpdateMarketV1Request::longitude).isNotNull()
        validate(UpdateMarketV1Request::latitude).isNotNull()
        validate(UpdateMarketV1Request::setCens).isNotNull()
        validate(UpdateMarketV1Request::area).isPositive()
        validate(UpdateMarketV1Request::districtCode).isPositive()
        validate(UpdateMarketV1Request::district).isNotEmpty()
        validate(UpdateMarketV1Request::townCode).isPositive()
        validate(UpdateMarketV1Request::town).isNotEmpty()
        validate(UpdateMarketV1Request::firstZone).isNotEmpty()
        validate(UpdateMarketV1Request::secondZone).isNotEmpty()
        validate(UpdateMarketV1Request::name).isNotEmpty()
        validate(UpdateMarketV1Request::publicArea).isNotEmpty()
        validate(UpdateMarketV1Request::neighborhood).isNotEmpty()
    }
}

fun UpdateMarketV1Request.toModel(registryCode: String) = Market(
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
