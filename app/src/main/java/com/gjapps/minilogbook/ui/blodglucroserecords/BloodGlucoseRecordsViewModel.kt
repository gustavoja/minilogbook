package com.gjapps.minilogbook.ui.blodglucroserecords

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepository
import com.gjapps.minilogbook.domain.usecases.ConverMgDlToMmollUseCase
import com.gjapps.minilogbook.domain.usecases.ConverMmollToMgDlUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertFromCurrentLanguageDecimalFormatUseCase
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageDecimalFormatUseCase
import com.gjapps.minilogbook.domain.usecases.DateToLocaleStringFormatUseCase
import com.gjapps.minilogbook.domain.usecases.SanitizeDecimalNumberUseCase
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
                                                       private val sanitizeDecimalNumber: SanitizeDecimalNumberUseCase,
                                                       private val dateToLocaleStringFormatUseCase: DateToLocaleStringFormatUseCase,
                                                       private val convertFromCurrentLanguageDecimalFormat: ConvertFromCurrentLanguageDecimalFormatUseCase,
                                                       private val convertToCurrentLanguageDecimalFormat: ConvertToCurrentLanguageDecimalFormatUseCase,
                                                       private val convertMmollToMgDl: ConverMmollToMgDlUseCase,
                                                       private val converMgDlToMmoll: ConverMgDlToMmollUseCase
                                                       ) : ViewModel() {

    private var bloodGlucoseRecordsListSuscription : Job? = null
    private var bloodGlucoseAvergaeSuscription : Job? = null

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
                    convertToCurrentlySelectedUnit(record.mgdlValue,unit)
                ),
                dateToLocaleStringFormatUseCase(record.date)) }

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

    fun onViewResumed()
    {
        subscribedToGlucoseRecordsFlow()
        subscribeToGlucoseAverageFlow()
    }

    fun onFilterChanged(selected: BloodGlucoseUnit) {
        if(selected == uiState.value.selectedUnit)
            return

        selectedBloodGlucoseUnitState.value = selected
        reloadNewRecordUserInput(selected)
    }

    fun onNewRecordValueChanged(newValue: String) {
        val currentValue = _uiState.value.newRecordUserInputValue
        val newValue = sanitizeDecimalNumber(currentValue, newValue)
        val isOnlyDecimalSeparator = newValue.length == 1 && newValue[0] == sanitizeDecimalNumber.getDecimalSeparatorForCurrentLocale()
        _uiState.update {
            it.copy(newRecordUserInputValue = newValue, isValidNewRecordInput = newValue.isNotEmpty() && !isOnlyDecimalSeparator)
        }
    }

    fun onSaveRecordValue() {
        val record = _uiState.value.newRecordUserInputValue
        if(record.isEmpty() || !uiState.value.isValidNewRecordInput)
            return

        addRecord(record)
        _uiState.update {
            it.copy(newRecordUserInputValue = "",isValidNewRecordInput = false)
        }
    }

    private fun subscribedToGlucoseRecordsFlow() {
        bloodGlucoseRecordsListSuscription?.cancel()
        bloodGlucoseRecordsListSuscription = viewModelScope.launch(exceptionHandler) {
            bloodGlucoseRecordsListState.collect()
        }
    }

    private fun subscribeToGlucoseAverageFlow() {
        bloodGlucoseAvergaeSuscription?.cancel()
        bloodGlucoseAvergaeSuscription = viewModelScope.launch(exceptionHandler) {
            bloodGlucoseRecordsRepository.bloodGlucoseAverage
                .combine(bloodGlucoseRecordsListState){ average, list ->
                    var convertedAverage = ""
                    if(list is BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords) {
                        convertedAverage = convertToCurrentLanguageDecimalFormat(convertToCurrentlySelectedUnit(average, uiState.value.selectedUnit))
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
                .collect()
        }
    }

    private fun addRecord(value: String){
        viewModelScope.launch(exceptionHandler) {
            var convertedValue = convertFromCurrentLanguageDecimalFormat(value)
            if(uiState.value.selectedUnit == BloodGlucoseUnit.Mmoldl)
                convertedValue = convertMmollToMgDl(convertedValue)
            bloodGlucoseRecordsRepository.saveRecord(convertedValue)
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

    private fun convertToCurrentlySelectedUnit(value: Float, unit: BloodGlucoseUnit = uiState.value.selectedUnit): Float {
        return if(unit == BloodGlucoseUnit.Mgdl) value else converMgDlToMmoll(value)
    }

    private fun reloadNewRecordUserInput(selected: BloodGlucoseUnit) {
        if(_uiState.value.isValidNewRecordInput) {
            var convertedValue = convertFromCurrentLanguageDecimalFormat(_uiState.value.newRecordUserInputValue)
            convertedValue =
                when (selected) {
                    BloodGlucoseUnit.Mgdl -> convertMmollToMgDl(convertedValue)
                    BloodGlucoseUnit.Mmoldl -> converMgDlToMmoll( convertedValue)
                    else -> return
                }
            onNewRecordValueChanged(convertToCurrentLanguageDecimalFormat(convertedValue))
        }
    }
}

