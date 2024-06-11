package com.gjapps.minilogbook.domain.usecases

import java.text.DecimalFormat
import java.util.Locale

interface ConvertToCurrentLanguageFormatUseCase {
    operator fun invoke(value: Float): String
}

class ConvertToCurrentLanguageFormatUseCaseImpl: ConvertToCurrentLanguageFormatUseCase {
    override operator fun invoke(decimalNumber: Float): String {
        val locale = Locale.getDefault()
        val decimalFormat = DecimalFormat.getInstance(locale)
        decimalFormat.maximumFractionDigits = 4
        return decimalFormat.format(decimalNumber)
    }
}