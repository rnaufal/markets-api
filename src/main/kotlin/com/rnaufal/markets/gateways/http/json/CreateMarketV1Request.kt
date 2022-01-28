package com.rnaufal.markets.gateways.http.json

import com.rnaufal.markets.domains.Market
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isPositive
import org.valiktor.validate

data class CreateMarketV1Request(
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
    val number: String? = null,
    val neighborhood: String,
    val reference: String? = null,
)

fun CreateMarketV1Request.validate() {
    validate(this) {
        validate(CreateMarketV1Request::legacyIdentifier).isGreaterThan(0)
        validate(CreateMarketV1Request::longitude).isNotNull()
        validate(CreateMarketV1Request::latitude).isNotNull()
        validate(CreateMarketV1Request::setCens).isNotNull()
        validate(CreateMarketV1Request::area).isPositive()
        validate(CreateMarketV1Request::districtCode).isPositive()
        validate(CreateMarketV1Request::district).isNotEmpty()
        validate(CreateMarketV1Request::townCode).isPositive()
        validate(CreateMarketV1Request::town).isNotEmpty()
        validate(CreateMarketV1Request::firstZone).isNotEmpty()
        validate(CreateMarketV1Request::secondZone).isNotEmpty()
        validate(CreateMarketV1Request::name).isNotEmpty()
        validate(CreateMarketV1Request::registryCode).isNotEmpty()
        validate(CreateMarketV1Request::publicArea).isNotEmpty()
        validate(CreateMarketV1Request::neighborhood).isNotEmpty()
    }
}

fun CreateMarketV1Request.toModel() = Market(
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
