package com.gjapps.minilogbook.domain.usecases

import java.text.DecimalFormat
import java.util.Locale

interface ConvertFloatToLocaleDecimalStringUseCase {
    operator fun invoke(value: Float): String
}

class ConvertFloatToLocaleDecimalStringUseCaseImpl: ConvertFloatToLocaleDecimalStringUseCase {
    override operator fun invoke(value: Float): String {
        val locale = Locale.getDefault()
        val decimalFormat = DecimalFormat.getInstance(locale)
        decimalFormat.maximumFractionDigits = 6
        return decimalFormat.format(value)
    }
}