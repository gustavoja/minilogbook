package com.gjapps.minilogbook.domain.usecases

import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepository
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.uistates.BloodGlucoseRecordItemUIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface GetLocalisedBloodGlucoseRecordsUseCase {
    operator fun invoke (recordsUnit: StateFlow<BloodGlucoseUnit>): Flow<List<BloodGlucoseRecordItemUIState>>
}

class GetLocalisedBloodGlucoseRecordsUseCaseImpl @Inject
constructor(private val bloodGlucoseRecordsRepository: BloodGlucoseRepository,
            private val convertToCurrentLanguageDateFormat: ConvertToCurrentLanguageDateFormatUseCase,
            private val convertToCurrentLanguageDecimalFormat: ConvertToCurrentLanguageFormatUseCase,
            private val convertBloodGlucoseUnit : ConvertBloodGlucoseUnitUseCase
):GetLocalisedBloodGlucoseRecordsUseCase {
    override fun invoke(recordsUnit: StateFlow<BloodGlucoseUnit>): Flow<List<BloodGlucoseRecordItemUIState>> {
        return bloodGlucoseRecordsRepository
            .bloodGlucoseRecords
            .combine(recordsUnit) { list, unit ->
                return@combine list.map { record ->
                    BloodGlucoseRecordItemUIState(
                        convertToCurrentLanguageDecimalFormat(
                            convertBloodGlucoseUnit(
                                record.mgdlValue,
                                BloodGlucoseUnit.Mgdl,
                                unit
                            )
                        ),
                        convertToCurrentLanguageDateFormat(record.date)
                    )
                }
            }
    }
}