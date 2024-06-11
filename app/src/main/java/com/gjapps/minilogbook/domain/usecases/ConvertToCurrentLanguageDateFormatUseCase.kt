package com.gjapps.minilogbook.domain.usecases

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface ConvertToCurrentLanguageDateFormatUseCase {
    operator fun invoke (date: Date, locale: Locale = Locale.getDefault()): String
}

class ConvertToCurrentLanguageDateFormatUseCaseImpl:ConvertToCurrentLanguageDateFormatUseCase {
    override operator fun invoke (date: Date, locale: Locale): String {
        val dateTimePattern = (SimpleDateFormat.getDateTimeInstance(
            SimpleDateFormat.MEDIUM,
            SimpleDateFormat.MEDIUM, locale) as SimpleDateFormat).toLocalizedPattern()
        val formatter = SimpleDateFormat(dateTimePattern)
        return formatter.format(date)
    }
}