package com.gjapps.minilogbook.ui.blodglucroserecords

import com.gjapps.minilogbook.data.models.BloodGlucoseUnit

sealed interface BloodGlucoseRecordsUiState {
    object Loading : BloodGlucoseRecordsUiState
    data class Empty(val inputValue:String, val selectedUnit: BloodGlucoseUnit) :
        BloodGlucoseRecordsUiState
    data class Records(val inputValue:String, val selectedUnit: BloodGlucoseUnit, val records: List<String>) :
        BloodGlucoseRecordsUiState
}