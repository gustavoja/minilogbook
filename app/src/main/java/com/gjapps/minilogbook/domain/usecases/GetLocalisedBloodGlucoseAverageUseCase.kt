package com.gjapps.minilogbook.domain.usecases

import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface GetLocalisedBloodGlucoseAverageUseCase {
    operator fun invoke (recordsUnit: StateFlow<BloodGlucoseUnit>): Flow<String>
}

class GetLocalisedBloodGlucoseAverageUseCaseImpl @Inject
constructor(private val bloodGlucoseRecordsRepository: BloodGlucoseRepository,
            private val convertToCurrentLanguageDecimalFormat: ConvertToCurrentLanguageFormatUseCase,
            private val convertBloodGlucoseUnit : ConvertBloodGlucoseUnitUseCase
):GetLocalisedBloodGlucoseAverageUseCase {
    override fun invoke(recordsUnit: StateFlow<BloodGlucoseUnit>): Flow<String> {
        return bloodGlucoseRecordsRepository
            .bloodGlucoseAverage
            .combine(recordsUnit){ average, unit ->
                convertToCurrentLanguageDecimalFormat(convertBloodGlucoseUnit(average, BloodGlucoseUnit.Mgdl, unit))
            }
            .combine(bloodGlucoseRecordsRepository.bloodGlucoseRecords){ average, records ->
                if(!records.any())
                    return@combine ""
                return@combine average
            }
    }
}