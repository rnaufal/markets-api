package com.rnaufal.markets.exceptions

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage
import org.valiktor.springframework.config.ValiktorConfiguration
import org.valiktor.springframework.http.ValiktorExceptionHandler
import org.valiktor.springframework.http.ValiktorResponse
import java.util.Locale

data class ValidationErrorResponse(val errors: List<ValidationError>)

data class ValidationError(
    val property: String,
    val value: String,
    val errorMessage: String
)

@Component
class ValidationExceptionHandler(
    private val config: ValiktorConfiguration
) : ValiktorExceptionHandler<ValidationErrorResponse> {

    override fun handle(exception: ConstraintViolationException, locale: Locale) =
        ValiktorResponse(
            statusCode = HttpStatus.BAD_REQUEST,
            body = ValidationErrorResponse(
                errors = exception.constraintViolations
                    .mapToMessage(baseName = config.baseBundleName, locale = locale)
                    .map { ValidationError(it.property, it.value.toString(), it.message) }
            )
        )
}
