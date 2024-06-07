package com.gjapps.minilogbook.ui.blodglucroserecords

import com.gjapps.minilogbook.data.models.BloodGlucoseUnit

data class BloodGlucoseRecordsUiState (
    val average:String,
    val newRecordInputValue:String,
    val selectedUnit: BloodGlucoseUnit,
    val loading : Boolean = false,
    val recordsState : RecordsState = RecordsState.Empty
)

sealed interface RecordsState {
    data object Empty : RecordsState
    data class WithRecords(val records: List<String>) : RecordsState
}