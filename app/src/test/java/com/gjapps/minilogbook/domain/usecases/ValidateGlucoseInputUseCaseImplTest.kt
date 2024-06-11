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

    @Before
    fun setup() {
        sanitizeDecimalNumber = mock {
            on { getDecimalSeparatorForCurrentLocale() } doReturn '.'
        }
        validateGlucoseInputUseCase = ValidateGlucoseInputUseCaseImpl(sanitizeDecimalNumber)
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