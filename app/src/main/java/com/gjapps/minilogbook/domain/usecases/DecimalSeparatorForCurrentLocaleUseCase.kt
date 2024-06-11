package com.gjapps.minilogbook.domain.usecases

import java.text.DecimalFormatSymbols

interface DecimalSeparatorForCurrentLocaleUseCase {
    operator fun invoke():Char
}
class DecimalSeparatorForCurrentLocaleUseCaseImpl:DecimalSeparatorForCurrentLocaleUseCase {
    override operator fun invoke(): Char {
        val symbols = DecimalFormatSymbols.getInstance()
        return symbols.decimalSeparator
    }
}
