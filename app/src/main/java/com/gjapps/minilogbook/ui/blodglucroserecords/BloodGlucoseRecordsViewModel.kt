package com.gjapps.minilogbook.ui.blodglucroserecords

import androidx.lifecycle.ViewModel
import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.domain.usecases.SanitizeDecimalNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BloodGlucoseRecordsViewModel @Inject constructor(private val sanitizeDecimalNumberUseCase: SanitizeDecimalNumberUseCase) : ViewModel() {
    private val _uiState: MutableStateFlow<BloodGlucoseRecordsUiState> = MutableStateFlow(
        BloodGlucoseRecordsUiState("", "", BloodGlucoseUnit.Mgdl, records =  RecordsState.Empty)
    )
    val uiState = _uiState.asStateFlow()

    fun onRecordValueChanged(newValue: String) {
        val currentValue = _uiState.value.newRecordInputValue
        _uiState.update {
            it.copy(newRecordInputValue = sanitizeDecimalNumberUseCase(currentValue, newValue))
        }
    }

    fun onFilterChanged(selected: BloodGlucoseUnit) {
        _uiState.update {
            it.copy(selectedUnit = selected)
        }
    }

    fun onSaveRecordValue() {
        _uiState.update {
            it.copy(newRecordInputValue = "", records = RecordsState.WithRecords(listOf(it.newRecordInputValue)))
        }
    }
}
