package com.rnaufal.markets.exceptions

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage
import org.valiktor.springframework.config.ValiktorConfiguration
import org.valiktor.springframework.http.ValiktorExceptionHandler
import org.valiktor.springframework.http.ValiktorResponse
import java.util.Locale

data class ErrorResponse(val errors: List<ErrorDetail>)

data class ErrorDetail(
    val property: String,
    val value: String,
    val errorMessage: String
)

@Component
class ValidationExceptionHandler(
    private val config: ValiktorConfiguration
) : ValiktorExceptionHandler<ErrorResponse> {

    override fun handle(exception: ConstraintViolationException, locale: Locale) =
        ValiktorResponse(
            statusCode = HttpStatus.BAD_REQUEST,
            body = ErrorResponse(
                errors = exception.constraintViolations
                    .mapToMessage(baseName = config.baseBundleName, locale = locale)
                    .map { ErrorDetail(it.property, it.value.toString(), it.message) }
            )
        )
}
