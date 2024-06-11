package com.gjapps.minilogbook.domain.usecases

import org.junit.Before
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import strikt.api.expectThat

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
        val newValue = "4"
        val expectedResult = "1234"

        val result = sanitizeDecimalNumberUseCase(currentValue, newValue)

        expectThat(expectedResult).equals(result)
    }

    @Test
    fun whenUseCaseExecuted_WithValidSeparator_ExpectNewValueAdded() {
        val currentValue = "123"
        val newValue = "4.5"
        val expectedResult = "1234.5"

        val result = sanitizeDecimalNumberUseCase(currentValue, newValue)

        expectThat(expectedResult).equals(result)
    }

    @Test
    fun whenUseCaseExecuted_WithCommaSeparator_ExpectNewValueAdded() {
        val currentValue = "123"
        val newValue = "4,5"
        val expectedResult = "1234.5"

        val result = sanitizeDecimalNumberUseCase(currentValue, newValue)

        expectThat(expectedResult).equals(result)
    }

    @Test
    fun whenUseCaseExecuted_WithMultipleDecimalSeparator_ExpectNewValueNotAdded() {
        val currentValue = "123.4"
        val newValue = "5.6"

        val result = sanitizeDecimalNumberUseCase(currentValue, newValue)

        expectThat(currentValue).equals(result)
    }

    @Test
    fun whenUseCaseExecuted_WithOtherValues_ExpectInvalidValuesNotAdded() {
        val currentValue = "123"
        val newValue = "a4b.5c"
        val expectedResult = "1234.5"

        val result = sanitizeDecimalNumberUseCase(currentValue, newValue)

        expectThat(expectedResult).equals(result)
    }
}