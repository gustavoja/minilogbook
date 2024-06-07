package com.gjapps.minilogbook.domain.usecases

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface DateToLocaleStringFormatUseCase {
    operator fun invoke (date: Date, locale: Locale = Locale.getDefault()): String
}

class DateToLocaleStringFormatUseCaseImpl:DateToLocaleStringFormatUseCase {
    override operator fun invoke (date: Date, locale: Locale): String {
        val dateTimePattern = (SimpleDateFormat.getDateTimeInstance(
            SimpleDateFormat.MEDIUM,
            SimpleDateFormat.MEDIUM, locale) as SimpleDateFormat).toLocalizedPattern()
        val formatter = SimpleDateFormat(dateTimePattern)
        return formatter.format(date)
    }
}