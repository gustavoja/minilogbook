package com.gjapps.minilogbook.domain.usecases

import java.text.DecimalFormat
import java.util.Locale

interface ConvertFromCurrentLanguageDecimalFormatUseCase {
    operator fun invoke (value: String): Float
}

class ConvertFromCurrentLanguageDecimalFormatUseCaseImpl:ConvertFromCurrentLanguageDecimalFormatUseCase {
    override operator fun invoke (value: String): Float {
        val locale = Locale.getDefault()
        val decimalFormat = DecimalFormat.getInstance(locale)
        return decimalFormat.parse(value).toFloat()
    }
}