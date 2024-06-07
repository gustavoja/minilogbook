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
        BloodGlucoseRecordsUiState.Records("", BloodGlucoseUnit.Mgdl, emptyList())
    )
    val uiState = _uiState.asStateFlow()

    fun onRecordValueChanged(newValue: String) {
        val currentValue = (_uiState.value as BloodGlucoseRecordsUiState.Records).inputValue
        _uiState.update {
            (it as BloodGlucoseRecordsUiState.Records).copy(inputValue = sanitizeDecimalNumberUseCase(currentValue, newValue))
        }
    }

    fun onFilterChanged(selected: BloodGlucoseUnit) {
        _uiState.update {
            (it as BloodGlucoseRecordsUiState.Records).copy(selectedUnit = selected)
        }
    }

    fun onSaveRecordValue() {
        _uiState.update {
            (it as BloodGlucoseRecordsUiState.Records).copy(inputValue = "")
        }
    }
}
