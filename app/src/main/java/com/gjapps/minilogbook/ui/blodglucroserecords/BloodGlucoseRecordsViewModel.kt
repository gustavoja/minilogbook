package com.gjapps.minilogbook.ui.blodglucroserecords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepository
import com.gjapps.minilogbook.domain.usecases.ConverMgDlToMmollUseCase
import com.gjapps.minilogbook.domain.usecases.ConverMmollToMgDlUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertFloatToLocaleDecimalStringUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertLocaleDecimalStringFloatUseCase
import com.gjapps.minilogbook.domain.usecases.DateToLocaleStringFormatUseCase
import com.gjapps.minilogbook.domain.usecases.SanitizeDecimalNumberUseCase
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.uistates.BloodGlucoseRecordItemUIState
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.uistates.BloodGlucoseRecordsListUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BloodGlucoseRecordsViewModel @Inject constructor(private val bloodGlucoseRecordsRepository: BloodGlucoseRepository,
                                                       private val sanitizeDecimalNumberUseCase: SanitizeDecimalNumberUseCase,
                                                       private val dateToLocaleStringFormatUseCase: DateToLocaleStringFormatUseCase,
                                                       private val convertLocaleDecimalStringFloatUseCase: ConvertLocaleDecimalStringFloatUseCase,
                                                       private val convertFloatToLocaleDecimalStringUseCase: ConvertFloatToLocaleDecimalStringUseCase,
                                                       private val convertMmollToMgDlUseCase: ConverMmollToMgDlUseCase,
                                                       private val converMgDlToMmollUseCase: ConverMgDlToMmollUseCase,

                                                       ) : ViewModel() {
    private val _uiState: MutableStateFlow<BloodGlucoseRecordsUiState> = MutableStateFlow(
        BloodGlucoseRecordsUiState("", "", BloodGlucoseUnit.Mgdl, recordsState =  BloodGlucoseRecordsListUIState.Empty)
    )
    val  uiState = _uiState.asStateFlow()

    private val bloodGlucoseRecordsListUiState = bloodGlucoseRecordsRepository
        .bloodGlucoseRecords
        .map{
            val recordsUIStates = it.map { record -> BloodGlucoseRecordItemUIState(
                convertFloatToLocaleDecimalStringUseCase(
                    if(uiState.value.selectedUnit ==  BloodGlucoseUnit.Mgdl) record.mgdlValue else converMgDlToMmollUseCase(record.mgdlValue)
                ),
                dateToLocaleStringFormatUseCase(record.date)) }
            if(it.any()) BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords(recordsUIStates) else BloodGlucoseRecordsListUIState.Empty
        }
        .catch {
            _uiState.update {
                it.copy(recordsState = BloodGlucoseRecordsListUIState.Error)
            }
        }
        .onEach { list ->
            _uiState.update {
                it.copy(recordsState = list)
            }
        }

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

        reloadRecords()
    }

    fun onSaveRecordValue() {
        val record = _uiState.value.newRecordInputValue
        if(record.isEmpty())
            return

        addRecord(record)
        _uiState.update {
            it.copy(newRecordInputValue = "")
        }
    }

    fun onLoaded()
    {
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            _uiState.update {
                it.copy(recordsState = BloodGlucoseRecordsListUIState.Error)
            }
        }) {
            bloodGlucoseRecordsListUiState.collect()
        }
    }

    private fun reloadRecords() {
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            _uiState.update {
                it.copy(recordsState = BloodGlucoseRecordsListUIState.Error)
            }
        }) {
            bloodGlucoseRecordsRepository.reloadBloodGlucoseRecords()
        }
    }

    private fun addRecord(value: String){
        viewModelScope.launch (CoroutineExceptionHandler{ _, exception ->
            _uiState.update {
                it.copy(recordsState = BloodGlucoseRecordsListUIState.Error)
            }
        }) {
            var convertedValue = convertLocaleDecimalStringFloatUseCase(value)
            convertedValue = if(uiState.value.selectedUnit ==  BloodGlucoseUnit.Mgdl) convertedValue else convertMmollToMgDlUseCase(convertedValue)
            bloodGlucoseRecordsRepository.saveRecord(convertedValue)
        }
    }
}

