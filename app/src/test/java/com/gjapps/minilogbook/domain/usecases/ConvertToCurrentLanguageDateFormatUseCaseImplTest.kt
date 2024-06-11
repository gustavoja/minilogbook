package com.gjapps.minilogbook.domain.usecases

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.util.Date
import java.util.Locale

class ConvertToCurrentLanguageDateFormatUseCaseImplTest{

    private val useCase = ConvertToCurrentLanguageDateFormatUseCaseImpl()

    @Test
    fun whenUseCaseExecuted_ExpectReturnDateFormattedAsFrenchStyle() {
        val date = Date(1672531200000)
        val locale = Locale.FRANCE
        val actualFormattedDate = useCase(date, locale)
        val expectedFormattedDate = "1 Jan 2023, 01:00:00"

        expectThat(expectedFormattedDate).isEqualTo(actualFormattedDate)
    }

    @Test
    fun whenUseCaseExecuted_ExpectReturnDateFormattedAsUsStyle() {
        val date = Date(1672531200000)
        val locale = Locale.US
        val actualFormattedDate = useCase(date,locale)
        val expectedFormattedDate = "Jan 1, 2023, 1:00:00 AM"
        expectThat(expectedFormattedDate).isEqualTo(actualFormattedDate)
    }
}