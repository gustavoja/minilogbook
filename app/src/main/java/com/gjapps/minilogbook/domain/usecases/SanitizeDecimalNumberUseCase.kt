package com.gjapps.minilogbook.domain.usecases

interface SanitizeDecimalNumberUseCase {
    operator fun invoke(currentValue:String,newValue: String): String
}

class SanitizeDecimalNumberUseCaseImpl(private val decimalSeparatorForCurrentLocaleUseCase:DecimalSeparatorForCurrentLocaleUseCase) : SanitizeDecimalNumberUseCase {
    override fun invoke(currentValue: String, newValue: String): String {
        val decimalSeparator = decimalSeparatorForCurrentLocaleUseCase()

        val newValue = newValue.replace('.', decimalSeparator).replace(',', decimalSeparator)

        if (newValue.count { it == decimalSeparator } > 1)
            return currentValue

        return removeNonDigitsExceptDecimalSeparators(newValue)
    }

    private fun removeNonDigitsExceptDecimalSeparators(input: String): String {
        val decimalSeparator = decimalSeparatorForCurrentLocaleUseCase()
        return input.filter { it.isDigit() || it == decimalSeparator }
    }
}