package com.gjapps.minilogbook.ui.blodglucroserecords

import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.uistates.BloodGlucoseRecordsListUIState

data class BloodGlucoseRecordsUiState (
    val average:String = "",
    val newRecordUserInputValue:String = "",
    val isValidNewRecordInput:Boolean = false,
    val selectedUnit: BloodGlucoseUnit = BloodGlucoseUnit.Mgdl,
    val recordsState : BloodGlucoseRecordsListUIState = BloodGlucoseRecordsListUIState.Loading
)