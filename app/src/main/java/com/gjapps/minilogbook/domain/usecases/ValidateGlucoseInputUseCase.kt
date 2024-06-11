package com.gjapps.minilogbook.domain.usecases

interface ValidateGlucoseInputUseCase {
    operator fun invoke (currentValue:String,newValue:String): Pair<String,Boolean>
}

class ValidateGlucoseInputUseCaseImpl(private val sanitizeDecimalNumber: SanitizeDecimalNumberUseCase):ValidateGlucoseInputUseCase {
    override operator fun invoke (currentValue:String,newValue:String): Pair<String,Boolean> {
        val processedValue = sanitizeDecimalNumber(currentValue, newValue)
        val isOnlyDecimalSeparator = processedValue.length == 1 && processedValue[0] == sanitizeDecimalNumber.getDecimalSeparatorForCurrentLocale()
        val isValidValue = processedValue.isNotEmpty() && !isOnlyDecimalSeparator

        return Pair(processedValue,isValidValue)
    }
}