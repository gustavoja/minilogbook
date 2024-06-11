package com.gjapps.minilogbook.domain.usecases

import java.text.DecimalFormat
import java.util.Locale

interface ParseFromCurrentLanguageFormatUseCase {
    operator fun invoke (value: String): Float
}

class ParseFromCurrentLanguageFormatUseCaseImpl:ParseFromCurrentLanguageFormatUseCase {
    override operator fun invoke (stringDecimalNumber: String): Float {
        val locale = Locale.getDefault()
        val decimalFormat = DecimalFormat.getInstance(locale)
        return decimalFormat.parse(stringDecimalNumber).toFloat()
    }
}