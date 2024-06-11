package com.gjapps.minilogbook.ui.blodglucroserecords

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepository
import com.gjapps.minilogbook.domain.usecases.ConvertBloodGlucoseUnitUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageDateFormatUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageFormatUseCase
import com.gjapps.minilogbook.domain.usecases.ValidateGlucoseInputUseCase
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.uistates.BloodGlucoseRecordItemUIState
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.uistates.BloodGlucoseRecordsListUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BloodGlucoseRecordsViewModel @Inject constructor(private val bloodGlucoseRecordsRepository: BloodGlucoseRepository,
                                                       private val convertToCurrentLanguageDateFormatUseCase: ConvertToCurrentLanguageDateFormatUseCase,
                                                       private val convertToCurrentLanguageDecimalFormat: ConvertToCurrentLanguageFormatUseCase,
                                                       private val convertBloodGlucoseUnit : ConvertBloodGlucoseUnitUseCase,
                                                       private val validateGlucoseInput: ValidateGlucoseInputUseCase
                                                       ) : ViewModel() {

    private var bloodGlucoseRecordsListSubscription : Job? = null
    private var bloodGlucoseAverageSubscription : Job? = null

    private val exceptionHandler=CoroutineExceptionHandler { _, exception ->
        Log.e("BloodGlucoseRecordsViewModel", exception.message, exception)
        _uiState.update {
            it.copy(recordsState = BloodGlucoseRecordsListUIState.Error)
        }
    }

    private var selectedBloodGlucoseUnitState = MutableStateFlow(BloodGlucoseUnit.Mgdl)

    private val _uiState: MutableStateFlow<BloodGlucoseRecordsUiState> = MutableStateFlow(BloodGlucoseRecordsUiState())
    val uiState = _uiState
        .combine(selectedBloodGlucoseUnitState){ state, unit -> state.copy(selectedUnit = unit) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, _uiState.value)

    private val bloodGlucoseRecordsListState = bloodGlucoseRecordsRepository
        .bloodGlucoseRecords
        .combine(selectedBloodGlucoseUnitState){ list, unit ->
            val recordsUIStates = list.map { record -> BloodGlucoseRecordItemUIState(
                convertToCurrentLanguageDecimalFormat(
                    convertBloodGlucoseUnit(record.mgdlValue,BloodGlucoseUnit.Mgdl,uiState.value.selectedUnit)
                ),
                convertToCurrentLanguageDateFormatUseCase(record.date)) }

            if(list.any()) BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords(recordsUIStates) else BloodGlucoseRecordsListUIState.Empty
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

    private val bloodGlucoseAverage = bloodGlucoseRecordsRepository
        .bloodGlucoseAverage
        .combine(bloodGlucoseRecordsListState){ average, list ->
            var convertedAverage = ""
            if(list is BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords) {
                convertedAverage = convertToCurrentLanguageDecimalFormat(convertBloodGlucoseUnit(average, BloodGlucoseUnit.Mgdl, uiState.value.selectedUnit))
            }
            _uiState.update {
                it.copy(average = convertedAverage)
            }
        }
        .catch {
            _uiState.update {
                it.copy(recordsState = BloodGlucoseRecordsListUIState.Error)
            }
        }

    fun onViewReady() {
        subscribedToGlucoseRecordsFlow()
        subscribeToGlucoseAverageFlow()
    }

    fun onFilterChanged(selected: BloodGlucoseUnit) {
        if(selected == uiState.value.selectedUnit)
            return

        reloadNewRecordUserInput( selectedBloodGlucoseUnitState.value,selected)
        selectedBloodGlucoseUnitState.value = selected
    }

    fun onNewRecordValueChanged(newValue: String) {
        val currentValue = _uiState.value.newRecordUserInputValue
        val (processedValue, isValidValue) = validateGlucoseInput(currentValue, newValue)
        _uiState.update {
            it.copy(newRecordUserInputValue = processedValue, isValidNewRecordInput = isValidValue)
        }
    }

    fun onSaveRecordValue() {
        val newRecord = _uiState.value.newRecordUserInputValue
        if(newRecord.isEmpty() || !uiState.value.isValidNewRecordInput)
            return

        if(saveRecord(newRecord))
            _uiState.update {
                it.copy(newRecordUserInputValue = "",isValidNewRecordInput = false)
            }
    }

    fun onDeletedRecords() {
        viewModelScope.launch(exceptionHandler) {
            bloodGlucoseRecordsRepository.deleteRecords()
            _uiState.update {
                it.copy(average = "")
            }
        }
    }

    private fun subscribedToGlucoseRecordsFlow() {
        bloodGlucoseRecordsListSubscription?.cancel()
        bloodGlucoseRecordsListSubscription = viewModelScope.launch(exceptionHandler) {
            bloodGlucoseRecordsListState.collect()
        }
    }

    private fun subscribeToGlucoseAverageFlow() {
        bloodGlucoseAverageSubscription?.cancel()
        bloodGlucoseAverageSubscription = viewModelScope.launch(exceptionHandler) {
            bloodGlucoseAverage.collect()
        }
    }

    private fun saveRecord(value: String):Boolean
    {
        viewModelScope.launch(exceptionHandler) {
            var convertedValue = convertBloodGlucoseUnit(value,uiState.value.selectedUnit,BloodGlucoseUnit.Mgdl)
            bloodGlucoseRecordsRepository.saveRecord(convertedValue)
        }

        return true
    }

    private fun reloadNewRecordUserInput(fromUnit: BloodGlucoseUnit,toUnit: BloodGlucoseUnit) {
        if(_uiState.value.isValidNewRecordInput) {
            val convertedValue = convertBloodGlucoseUnit(_uiState.value.newRecordUserInputValue,fromUnit,toUnit)
            onNewRecordValueChanged(convertToCurrentLanguageDecimalFormat(convertedValue))
        }
    }
}

