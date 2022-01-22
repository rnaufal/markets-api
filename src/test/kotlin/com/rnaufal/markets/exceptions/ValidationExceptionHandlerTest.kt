package com.rnaufal.markets.exceptions

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.valiktor.ConstraintViolation
import org.valiktor.ConstraintViolationException
import org.valiktor.DefaultConstraintViolation
import org.valiktor.constraints.Greater
import org.valiktor.springframework.config.ValiktorConfiguration
import java.util.Locale

@ExtendWith(MockKExtension::class)
class ValidationExceptionHandlerTest(@MockK private val valiktorConfiguration: ValiktorConfiguration) {

    private val validationExceptionHandler = ValidationExceptionHandler(valiktorConfiguration)

    @Test
    fun `should handle validation errors`() {
        every { valiktorConfiguration.baseBundleName } returns "<<BUNDLE_NAME>>"

        val errorResponse = validationExceptionHandler.handle(
            buildConstraintViolationException(),
            locale = Locale.ENGLISH
        ).body

        assertThat(errorResponse.errors.size).isEqualTo(1)

        with(errorResponse.errors[0]) {
            assertThat(this.property).isEqualTo("age")
            assertThat(this.value).isEqualTo("12")
            assertThat(this.errorMessage).isNotBlank
        }

        verify { valiktorConfiguration.baseBundleName }
        confirmVerified(valiktorConfiguration)
    }

    private fun buildConstraintViolationException() =
        ConstraintViolationException(
            setOf<ConstraintViolation>(
                buildConstraintViolation()
            )
        )

    private fun buildConstraintViolation(): DefaultConstraintViolation {
        return DefaultConstraintViolation(
            property = "age",
            value = "12",
            constraint = Greater(10)
        )
    }
}
