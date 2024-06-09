package com.gjapps.minilogbook.domain.usecases

import java.text.DecimalFormat
import java.util.Locale

interface ConvertToCurrentLanguageDecimalFormatUseCase {
    operator fun invoke(value: Float): String
}

class ConvertToCurrentLanguageDecimalFormatUseCaseImpl: ConvertToCurrentLanguageDecimalFormatUseCase {
    override operator fun invoke(value: Float): String {
        val locale = Locale.getDefault()
        val decimalFormat = DecimalFormat.getInstance(locale)
        decimalFormat.maximumFractionDigits = 4
        return decimalFormat.format(value)
    }
}