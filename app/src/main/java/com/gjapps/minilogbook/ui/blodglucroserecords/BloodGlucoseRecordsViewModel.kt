package com.gjapps.minilogbook.ui.blodglucroserecords

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepository
import com.gjapps.minilogbook.domain.usecases.ApplyUnitAndLocaliseBloodGlucoseRecordUseCase
import com.gjapps.minilogbook.domain.usecases.GetLocalisedBloodGlucoseAverageUseCase
import com.gjapps.minilogbook.domain.usecases.GetLocalisedBloodGlucoseRecordsUseCase
import com.gjapps.minilogbook.domain.usecases.SaveRecordUseCase
import com.gjapps.minilogbook.domain.usecases.ValidateGlucoseInputUseCase
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.uistates.BloodGlucoseRecordsListUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BloodGlucoseRecordsViewModel @Inject constructor(private val bloodGlucoseRecordsRepository: BloodGlucoseRepository,
                                                       private val validateGlucoseInput: ValidateGlucoseInputUseCase,
                                                       getLocalisedBloodGlucoseRecords : GetLocalisedBloodGlucoseRecordsUseCase,
                                                       getLocalisedBloodGlucoseAverage: GetLocalisedBloodGlucoseAverageUseCase,
                                                       private val saveRecord: SaveRecordUseCase,
                                                       private val applyUnitAndLocaliseBloodGlucoseRecordUseCase: ApplyUnitAndLocaliseBloodGlucoseRecordUseCase
                                                       ) : ViewModel() {

    private var bloodGlucoseRecordsListSubscription : Job? = null
    private var bloodGlucoseAverageSubscription : Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e(TAG, exception.message, exception)
        _uiState.update {
            it.copy(recordsState = BloodGlucoseRecordsListUIState.Error)
        }
    }

    private var selectedBloodGlucoseUnitState = MutableStateFlow(BloodGlucoseUnit.Mgdl)

    private val _uiState: MutableStateFlow<BloodGlucoseRecordsUiState> = MutableStateFlow(BloodGlucoseRecordsUiState())
    val uiState = _uiState

    private val bloodGlucoseRecordsListState = getLocalisedBloodGlucoseRecords(selectedBloodGlucoseUnitState)
        .map {records ->
            if(records.any()) BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords(records) else BloodGlucoseRecordsListUIState.Empty
        }
        .onEach { recordsListState ->
            _uiState.update {
                it.copy(recordsState = recordsListState)
            }
        }

    private val bloodGlucoseAverage = getLocalisedBloodGlucoseAverage(selectedBloodGlucoseUnitState)
        .onEach{ average ->
            _uiState.update {
                it.copy(average = average)
            }
        }

    fun onViewReady() {
        subscribedToGlucoseRecordsFlow()
        subscribeToGlucoseAverageFlow()
    }

    fun onFilterChanged(selected: BloodGlucoseUnit) {
        if(selected == uiState.value.selectedUnit)
            return

        if(_uiState.value.isValidNewRecordInput)
            onNewRecordValueChanged(applyUnitAndLocaliseBloodGlucoseRecordUseCase(_uiState.value.newRecordUserInputValue,selectedBloodGlucoseUnitState.value,selected))

        selectedBloodGlucoseUnitState.value = selected

        _uiState.update {it.copy(selectedUnit =  selectedBloodGlucoseUnitState.value)}
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

        viewModelScope.launch(exceptionHandler) {
            saveRecord(newRecord,selectedBloodGlucoseUnitState.value)
            _uiState.update {
                it.copy(newRecordUserInputValue = "",isValidNewRecordInput = false)
            }
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

    companion object {
        private const val TAG = "BloodGlucoseRecordsViewModel"
    }
}

