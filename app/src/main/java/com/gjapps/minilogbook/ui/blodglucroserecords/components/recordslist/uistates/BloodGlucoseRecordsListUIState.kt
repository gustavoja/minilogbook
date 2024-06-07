package com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.uistates

sealed interface BloodGlucoseRecordsListUIState {
    data object Error : BloodGlucoseRecordsListUIState
    data object Empty : BloodGlucoseRecordsListUIState
    data class WithBloodGlucoseRecords(val records: List<BloodGlucoseRecordItemUIState>) : BloodGlucoseRecordsListUIState
}