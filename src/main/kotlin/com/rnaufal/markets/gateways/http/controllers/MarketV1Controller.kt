package com.rnaufal.markets.gateways.http.controllers

import com.rnaufal.markets.gateways.http.json.CreateMarketV1Request
import com.rnaufal.markets.gateways.http.json.toModel
import com.rnaufal.markets.gateways.http.json.toResponse
import com.rnaufal.markets.gateways.http.json.validate
import com.rnaufal.markets.usecases.MarketService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/markets")
class MarketV1Controller(private val marketService: MarketService) {

    @Operation(tags = ["Markets"], summary = "Create a market")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Returns the new created market."
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request for market creation."
            ),
        ]
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    suspend fun create(
        @RequestBody request: CreateMarketV1Request
    ) = request.validate().run { marketService.execute(request.toModel()) }.toResponse()

    @Operation(tags = ["Markets"], summary = "Delete a market by its registry code")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Market deleted successfully."
            ),
            ApiResponse(
                responseCode = "404",
                description = "Market not found."
            ),
        ]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{code}")
    suspend fun delete(
        @PathVariable code: String,
    ) = marketService.delete(code)

    @Operation(tags = ["Markets"], summary = "Get a market by its id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Returns Market by its id."
            ),
            ApiResponse(
                responseCode = "404",
                description = "Market not found."
            ),
        ]
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    suspend fun getById(
        @PathVariable id: String,
    ) = marketService.getById(id).toResponse()
}
