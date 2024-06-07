package com.gjapps.minilogbook.domain.usecases

import java.text.DecimalFormat
import java.util.Locale

interface ConvertLocaleDecimalStringFloatUseCase {
    operator fun invoke (value: String): Float
}

class ConvertLocaleDecimalStringFloatUseCaseImpl:ConvertLocaleDecimalStringFloatUseCase {
    override operator fun invoke (value: String): Float {
        val locale = Locale.getDefault()
        val decimalFormat = DecimalFormat.getInstance(locale)
        return decimalFormat.parse(value).toFloat()
    }
}