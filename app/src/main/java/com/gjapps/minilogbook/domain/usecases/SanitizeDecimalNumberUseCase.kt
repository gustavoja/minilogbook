package com.gjapps.minilogbook.domain.usecases

import java.text.DecimalFormatSymbols

interface SanitizeDecimalNumberUseCase {
    operator fun invoke(currentValue:String,newValue: String): String
    fun getDecimalSeparatorForCurrentLocale(): Char
}

class SanitizeDecimalNumberUseCaseImpl : SanitizeDecimalNumberUseCase {
    override fun invoke(currentValue: String, newValue: String): String {
        val decimalSeparator = getDecimalSeparatorForCurrentLocale()
        if(newValue.count { it == decimalSeparator } > 1)
            return currentValue

        return removeNonDigitsExceptDecimalSeparators(newValue)
    }

    fun removeNonDigitsExceptDecimalSeparators(input: String): String {
        val decimalSeparator = getDecimalSeparatorForCurrentLocale()
        return input.filter { it.isDigit() || it == decimalSeparator}
    }

    override fun getDecimalSeparatorForCurrentLocale(): Char {
        val symbols = DecimalFormatSymbols.getInstance()
        return symbols.decimalSeparator
    }
}