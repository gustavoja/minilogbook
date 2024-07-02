package com.gjapps.minilogbook.domain.usecases

import com.gjapps.minilogbook.data.models.BloodGlucoseUnit

interface ApplyUnitAndLocaliseBloodGlucoseRecordUseCase {
    operator fun invoke (bloodRecordValue:String, fromUnit: BloodGlucoseUnit, toUnit: BloodGlucoseUnit): String
}

class ApplyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl (
    private val convertToCurrentLanguageDecimalFormat: ConvertToCurrentLanguageFormatUseCase,
    private val convertBloodGlucoseUnit : ConvertBloodGlucoseUnitUseCase):ApplyUnitAndLocaliseBloodGlucoseRecordUseCase {
    override operator fun invoke (bloodRecordValue:String, fromUnit: BloodGlucoseUnit, toUnit: BloodGlucoseUnit): String {
        val convertedUnitBloodGlucoseRecord = convertBloodGlucoseUnit(bloodRecordValue,fromUnit,toUnit)
        val localisedBloodGlucoseRecord = convertToCurrentLanguageDecimalFormat(convertedUnitBloodGlucoseRecord)
        return localisedBloodGlucoseRecord
    }
}