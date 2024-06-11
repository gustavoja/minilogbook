package com.gjapps.minilogbook.domain.usecases

import java.text.DecimalFormat
import java.util.Locale

interface ParseFromCurrentLanguageFormatUseCase {
    operator fun invoke (value: String, locale: Locale=Locale.getDefault()): Float
}

class ParseFromLanguageFormatUseCaseImpl:ParseFromCurrentLanguageFormatUseCase {
    override operator fun invoke (stringDecimalNumber: String, locale: Locale): Float {
        val decimalFormat = DecimalFormat.getInstance(locale)
        return decimalFormat.parse(stringDecimalNumber).toFloat()
    }
}