package com.gjapps.minilogbook.domain.usecases

import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepository

interface SaveRecordUseCase {
    suspend operator fun invoke (value: String, selectedUnit: BloodGlucoseUnit)
}

class SaveRecordUseCaseImpl(private val bloodGlucoseRecordsRepository: BloodGlucoseRepository,
                            private val convertBloodGlucoseUnit : ConvertBloodGlucoseUnitUseCase) :SaveRecordUseCase {
    override suspend operator fun invoke (value: String, selectedUnit: BloodGlucoseUnit) {
        val convertedValue = convertBloodGlucoseUnit(value,selectedUnit,BloodGlucoseUnit.Mgdl)
        bloodGlucoseRecordsRepository.saveRecord(convertedValue)
    }
}