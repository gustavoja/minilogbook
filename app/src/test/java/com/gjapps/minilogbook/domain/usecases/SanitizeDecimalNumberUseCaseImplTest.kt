package com.gjapps.minilogbook.domain.usecases

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class SanitizeDecimalNumberUseCaseImplTest{

    private lateinit var decimalSeparatorForCurrentLocaleUseCase: DecimalSeparatorForCurrentLocaleUseCase
    private lateinit var sanitizeDecimalNumberUseCase: SanitizeDecimalNumberUseCaseImpl

    @Before
    fun setup() {
        decimalSeparatorForCurrentLocaleUseCase = mock {
            on { invoke() } doReturn '.'
        }
        sanitizeDecimalNumberUseCase = SanitizeDecimalNumberUseCaseImpl(decimalSeparatorForCurrentLocaleUseCase)
    }

    @Test
    fun whenUseCaseExecuted_WithValidNumber_ExpectNewValueAdded() {
        val currentValue = "123"
        val newValue = "1234"
        val expectedResult = "1234"

        val result = sanitizeDecimalNumberUseCase(currentValue, newValue)

        expectThat(expectedResult).isEqualTo(result)
    }

    @Test
    fun whenUseCaseExecuted_WithValidSeparator_ExpectNewValueAdded() {
        val currentValue = "1234"
        val newValue = "1234.5"
        val expectedResult = "1234.5"

        val result = sanitizeDecimalNumberUseCase(currentValue, newValue)

        expectThat(expectedResult).isEqualTo(result)
    }

    @Test
    fun whenUseCaseExecuted_WithCommaSeparator_ExpectNewValueAdded() {
        val currentValue = "123"
        val newValue = "123,4"
        val expectedResult = "123.4"

        val result = sanitizeDecimalNumberUseCase(currentValue, newValue)

        expectThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun whenUseCaseExecuted_WithMultipleDecimalSeparator_ExpectNewValueNotAdded() {
        val currentValue = "123.4"
        val newValue = "123.4."

        val result = sanitizeDecimalNumberUseCase(currentValue, newValue)

        expectThat(result).isEqualTo(currentValue)
    }

    @Test
    fun whenUseCaseExecuted_WithOtherValues_ExpectInvalidValuesNotAdded() {
        val currentValue = "123"
        val newValue = "123a4b.5c"
        val expectedResult = "1234.5"

        val result = sanitizeDecimalNumberUseCase(currentValue, newValue)

        expectThat(result ).isEqualTo(expectedResult)
    }
}