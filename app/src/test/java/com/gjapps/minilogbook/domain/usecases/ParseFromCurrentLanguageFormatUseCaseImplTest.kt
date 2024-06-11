package com.gjapps.minilogbook.domain.usecases

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.text.ParseException
import java.util.Locale

class ParseFromCurrentLanguageFormatUseCaseImplTest{
    private val useCase = ParseFromLanguageFormatUseCaseImpl()

    @Test
    fun  whenUseCaseExecuted_ExpectNumberWithDotSeparatorParsed() {
        val inputString = "123.45"
        val locale = Locale.US
        val expectedNumber = 123.45f

        val actualNumber = useCase(inputString, locale)

        assertEquals(expectedNumber, actualNumber, 0.001f)
    }

    @Test
    fun whenUseCaseExecuted_ExpectNumberWithCommaSeparatorParsed() {
        val inputString = "1234,56"
        val locale = Locale.FRANCE
        val expectedNumber = 1234.56f

        val actualNumber = useCase(inputString, locale)

        assertEquals(expectedNumber, actualNumber, 0.001f)
    }

    @Test(expected = ParseException::class)
    fun whenUseCaseExecuted_ExpectExceptionThrownOnNonParseableString() {
        val inputString = "abc"
        val locale = Locale.US

        useCase(inputString, locale)
    }
}