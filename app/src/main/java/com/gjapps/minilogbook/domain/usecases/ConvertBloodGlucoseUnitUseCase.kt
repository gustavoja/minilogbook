package com.gjapps.minilogbook.domain.usecases

import com.gjapps.minilogbook.data.models.BloodGlucoseUnit

interface ConvertBloodGlucoseUnitUseCase {
    operator fun invoke(value: Float, fromUnit: BloodGlucoseUnit, toUnit: BloodGlucoseUnit): Float
    operator fun invoke(value: String,fromUnit: BloodGlucoseUnit,toUnit: BloodGlucoseUnit):Float
}

class ConvertBloodGlucoseUnitUseCaseUseCaseImpl(private val convertMgDlToMmoll:ConvertMgDlToMmollUseCase,
                                                private val convertMmollToMgDl:ConverMmollToMgDlUseCase,
                                                private val convertFromCurrentLanguageDecimalFormat: ParseFromCurrentLanguageFormatUseCase) : ConvertBloodGlucoseUnitUseCase{
    override operator fun invoke(value: Float, fromUnit: BloodGlucoseUnit, toUnit: BloodGlucoseUnit): Float {
        return when (toUnit) {
            BloodGlucoseUnit.Mgdl -> if(fromUnit == BloodGlucoseUnit.Mgdl) value else convertMmollToMgDl(value)
            BloodGlucoseUnit.Mmoldl -> if(fromUnit == BloodGlucoseUnit.Mmoldl) value else convertMgDlToMmoll(value)
        }
    }

    override operator fun invoke(value:String,fromUnit: BloodGlucoseUnit,toUnit: BloodGlucoseUnit):Float{
        var convertedValue = convertFromCurrentLanguageDecimalFormat(value)
        return invoke(convertedValue,fromUnit,toUnit)
    }
}