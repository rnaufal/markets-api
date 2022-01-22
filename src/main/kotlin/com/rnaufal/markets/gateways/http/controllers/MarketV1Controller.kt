package com.rnaufal.markets.gateways.http.controllers

import com.rnaufal.markets.gateways.http.json.CreateMarketV1Request
import com.rnaufal.markets.gateways.http.json.toModel
import com.rnaufal.markets.gateways.http.json.toResponse
import com.rnaufal.markets.gateways.http.json.validate
import com.rnaufal.markets.usecases.CreateMarket
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/markets")
class MarketV1Controller(private val createMarket: CreateMarket) {

    @Operation(tags = ["Markets"], summary = "Create a market")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Returns the new created market."
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request for market creation"
            ),
        ]
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    suspend fun create(
        @RequestBody request: CreateMarketV1Request
    ) = request.validate().run { createMarket.execute(request.toModel()) }.toResponse()
}
