package com.rnaufal.markets.exceptions

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class DefaultExceptionHandler {

    companion object {
        private val logger = KotlinLogging.logger { }
    }

    data class ErrorResponse(val message: String)

    @ExceptionHandler(MarketNotFoundException::class)
    fun handleNotFoundException(marketNotFoundException: MarketNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error(marketNotFoundException) { marketNotFoundException.message }

        return marketNotFoundException.message?.let {
            ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(
                ErrorResponse(it)
            )
        }.let { responseEntity ->
            responseEntity ?: ResponseEntity.status(HttpStatus.NOT_FOUND.value()).build()
        }
    }
}
