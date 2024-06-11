package com.gjapps.minilogbook.domain.usecases

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock


class ValidateGlucoseInputUseCaseImplTest {

    private lateinit var sanitizeDecimalNumber: SanitizeDecimalNumberUseCase
    private lateinit var validateGlucoseInputUseCase: ValidateGlucoseInputUseCaseImpl
    private lateinit var decimalSeparatorForCurrentLocaleUseCase: DecimalSeparatorForCurrentLocaleUseCase

    @Before
    fun setup() {
        sanitizeDecimalNumber = Mockito.mock(SanitizeDecimalNumberUseCase::class.java)

        decimalSeparatorForCurrentLocaleUseCase = mock {
            on { invoke() } doReturn '.' // Customize the decimal separator as needed
        }
        validateGlucoseInputUseCase = ValidateGlucoseInputUseCaseImpl(sanitizeDecimalNumber,decimalSeparatorForCurrentLocaleUseCase)
    }

    @Test
    fun  whenUseCaseExecuted_ExpectReturnProcessedValueAndTrue () {
        val currentValue = "123"
        val newValue = "4"
        val expectedProcessedValue = "1234"

        Mockito.`when`(sanitizeDecimalNumber.invoke(any(), any())).thenReturn(expectedProcessedValue)

        val (actualProcessedValue, isValid) = validateGlucoseInputUseCase(currentValue, newValue)

        assertEquals(expectedProcessedValue, actualProcessedValue)
        assertEquals(true, isValid)
    }

    @Test
    fun whenUseCaseExecuted_WithEmptyInput_ExpectReturnEmptyAndValidFalse() {
        val currentValue = ""
        val newValue = ""

        Mockito.`when`(sanitizeDecimalNumber.invoke(any(), any())).thenReturn("")

        val (actualProcessedValue, isValid) = validateGlucoseInputUseCase(currentValue, newValue)

        assertEquals("", actualProcessedValue)
        assertEquals(false, isValid)
    }

    @Test
    fun whenUseCaseExecuted_WithOnlyDecimalSeparator_ExpectSeparatorAndValidFalse() {
        val currentValue = ""
        val newValue = "."

        Mockito.`when`(sanitizeDecimalNumber.invoke(any(), any())).thenReturn(".")

        val (actualProcessedValue, isValid) = validateGlucoseInputUseCase(currentValue, newValue)

        assertEquals(".", actualProcessedValue)
        assertEquals(false, isValid)
    }
}