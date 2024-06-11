package com.gjapps.minilogbook.ui.blodglucroserecords

import app.cash.turbine.test
import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepository
import com.gjapps.minilogbook.domain.usecases.ConvertMgDlToMmollUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConverMmollToMgDlUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertBloodGlucoseUnitUseCaseUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageDateFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ParseFromCurrentLanguageFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.SanitizeDecimalNumberUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ValidateGlucoseInputUseCaseImpl
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.uistates.BloodGlucoseRecordsListUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue
import java.util.Date

class BloodGlucoseRecordsViewModelTest{
    private lateinit var viewModel: BloodGlucoseRecordsViewModel
    private lateinit var bloodGlucoseRepositoryMock: BloodGlucoseRepository
    private lateinit var bloodGlucoseRecordsFlowMock : Flow<List<BloodGlucoseRecordModel>>
    private lateinit var bloodGlucoseAverageFlowMock : Flow<Float>

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        bloodGlucoseRepositoryMock = setupBloodRecordsRepositoryMock();
        createViewModel()
    }

    private fun createViewModel() {
        viewModel = BloodGlucoseRecordsViewModel(
            bloodGlucoseRepositoryMock,
            ConvertToCurrentLanguageDateFormatUseCaseImpl(),
            ConvertToCurrentLanguageFormatUseCaseImpl(),
            ConvertBloodGlucoseUnitUseCaseUseCaseImpl(
                ConvertMgDlToMmollUseCaseImpl(),
                ConverMmollToMgDlUseCaseImpl(),
                ParseFromCurrentLanguageFormatUseCaseImpl()
            ),
            ValidateGlucoseInputUseCaseImpl(SanitizeDecimalNumberUseCaseImpl())
        )
    }

    private fun setupBloodRecordsRepositoryMock(): BloodGlucoseRepository {
        bloodGlucoseRepositoryMock = mock(BloodGlucoseRepository::class.java)

        bloodGlucoseRecordsFlowMock = mock<Flow<List<BloodGlucoseRecordModel>>>()
        bloodGlucoseAverageFlowMock = mock<Flow<Float>>()

        `when`(bloodGlucoseRepositoryMock.bloodGlucoseRecords).thenReturn(bloodGlucoseRecordsFlowMock)
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseAverage).thenReturn(bloodGlucoseAverageFlowMock)

        return bloodGlucoseRepositoryMock
    }

    @Test
    fun whenOnDeletedRecordsExecuted_ExpectACallToDeleteRecordsOnTheBloodRecordsRepository() = runTest{
        //act
        viewModel.onDeletedRecords()

        //assert
        verify(bloodGlucoseRepositoryMock, Mockito.times(1)).deleteRecords()
    }

    @Test
    fun whenOnSaveRecordValueExecuted_ExpectACallToSaveRecordsOnTheBloodRecordsRepository_IfTheStateOfRecordValueIsValid() = runTest{
        //act
        viewModel.onNewRecordValueChanged("1")
        viewModel.onSaveRecordValue()

        //assert
        verify(bloodGlucoseRepositoryMock, Mockito.times(1)).saveRecord(1f)
    }

    @Test
    fun whenOnSaveRecordValueExecuted_ExpectNewRecordUserInputValueSetToEmptyOnState_WasSaved() = runTest{
        //act
        viewModel.onNewRecordValueChanged("1")
        viewModel.onSaveRecordValue()

        //assert
        verify(bloodGlucoseRepositoryMock, Mockito.times(1)).saveRecord(1f)
        viewModel.uiState.test {
            val item = awaitItem()
            expectThat(item.newRecordUserInputValue).equals("")
            expectNoEvents()
        }
    }

    @Test
    fun whenOnNewRecordValueChanged_ExpectNewRecordUserInputValueSetOnState_IfTheStateOfRecordValueIsValid() = runTest{
        //act
        viewModel.onNewRecordValueChanged("1")

        //assert
        viewModel.uiState.test {
            val item = awaitItem()
            expectThat(item.newRecordUserInputValue).equals("1")
            expectNoEvents()
        }
    }

    @Test
    fun whenOnSaveRecordValueExecuted_ExpectValueSetOnState_IfTheStateOfRecordValueIsValid() = runTest{
        //act
        viewModel.onNewRecordValueChanged("1")

        //assert
        viewModel.uiState.test {
            val item = awaitItem()
            expectThat(item.newRecordUserInputValue).equals("1")
            expectNoEvents()
        }
    }

    @Test
    fun whenOnSaveRecordValueExecuted_ExpectValueMarkedAsValidSetOnState_IfTheStateOfRecordValueIsValid() = runTest{
        //act
        viewModel.onNewRecordValueChanged("1")

        //assert
        viewModel.uiState.test {
            val item = awaitItem()
            expectThat(item.isValidNewRecordInput).isTrue()
            expectNoEvents()
        }
    }

    @Test
    fun whenRecordListEmittedFromTheRepo_ExpectThatUiStatRecordsIsChangedToWithValues() = runTest{
        //arrange
        bloodGlucoseRepositoryMock = mock(BloodGlucoseRepository::class.java)
        val testFlow = MutableStateFlow<List<BloodGlucoseRecordModel>>(listOf())
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseRecords).thenReturn(testFlow)
        val bloodGlucoseAverageFlowMock = mock<Flow<Float>>()
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseAverage).thenReturn(bloodGlucoseAverageFlowMock)

        viewModel = BloodGlucoseRecordsViewModel(
            bloodGlucoseRepositoryMock,
            ConvertToCurrentLanguageDateFormatUseCaseImpl(),
            ConvertToCurrentLanguageFormatUseCaseImpl(),
            ConvertBloodGlucoseUnitUseCaseUseCaseImpl(
                ConvertMgDlToMmollUseCaseImpl(),
                ConverMmollToMgDlUseCaseImpl(),
                ParseFromCurrentLanguageFormatUseCaseImpl()
            ),
            ValidateGlucoseInputUseCaseImpl(SanitizeDecimalNumberUseCaseImpl())
        )

        //act
        viewModel.onViewReady()

        //assert
        viewModel.uiState.test {
            var uiState = awaitItem()
            expectThat(uiState.recordsState).isA<BloodGlucoseRecordsListUIState.Empty>()
            testFlow.value = listOf(BloodGlucoseRecordModel(1f, Date()))
            uiState = awaitItem()
            expectThat(uiState.recordsState).isA<BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords>()
            expectNoEvents()
        }
    }

    @Test
    fun whenRecordListmittedFromTheRepo_ExpectThatUiStateIsUpdatedWithTheTasksEmitted() = runTest{
        //arrange
        var bloodGlucoseRepositoryMock = mock(BloodGlucoseRepository::class.java)
        val testFlow = MutableStateFlow<List<BloodGlucoseRecordModel>>(listOf())
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseRecords).thenReturn(testFlow)
        val bloodGlucoseAverageFlowMock = mock<Flow<Float>>()
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseAverage).thenReturn(bloodGlucoseAverageFlowMock)

        var viewModel = BloodGlucoseRecordsViewModel(
            bloodGlucoseRepositoryMock,
            ConvertToCurrentLanguageDateFormatUseCaseImpl(),
            ConvertToCurrentLanguageFormatUseCaseImpl(),
            ConvertBloodGlucoseUnitUseCaseUseCaseImpl(
                ConvertMgDlToMmollUseCaseImpl(),
                ConverMmollToMgDlUseCaseImpl(),
                ParseFromCurrentLanguageFormatUseCaseImpl()
            ),
            ValidateGlucoseInputUseCaseImpl(SanitizeDecimalNumberUseCaseImpl())
        )

        //act
        viewModel.onViewReady()

        //assert
        viewModel.uiState.test {
            var uiState = awaitItem()
            expectThat(uiState.recordsState).isA<BloodGlucoseRecordsListUIState.Empty>()
            testFlow.value = listOf(BloodGlucoseRecordModel(1f, Date()))
            uiState = awaitItem()
            expectThat(uiState.recordsState).isA<BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords>()
            expectNoEvents()
        }
    }

    @Test
    fun whenSelectedUnitChanged_ExpectThatTheUnitOnRecordIsTransformed() = runTest{
        //arrange
        var bloodGlucoseRepositoryMock = mock(BloodGlucoseRepository::class.java)
        val testFlow = MutableStateFlow<List<BloodGlucoseRecordModel>>(listOf())
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseRecords).thenReturn(testFlow)
        val bloodGlucoseAverageFlowMock = mock<Flow<Float>>()
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseAverage).thenReturn(bloodGlucoseAverageFlowMock)

        var viewModel = BloodGlucoseRecordsViewModel(
            bloodGlucoseRepositoryMock,
            ConvertToCurrentLanguageDateFormatUseCaseImpl(),
            ConvertToCurrentLanguageFormatUseCaseImpl(),
            ConvertBloodGlucoseUnitUseCaseUseCaseImpl(
                ConvertMgDlToMmollUseCaseImpl(),
                ConverMmollToMgDlUseCaseImpl(),
                ParseFromCurrentLanguageFormatUseCaseImpl()
            ),
            ValidateGlucoseInputUseCaseImpl(SanitizeDecimalNumberUseCaseImpl())
        )

        //act
        viewModel.onViewReady()

        //assert
        viewModel.uiState.test {
            awaitItem()
            testFlow.value = listOf(BloodGlucoseRecordModel(1f, Date()))
            awaitItem()
            viewModel.onFilterChanged(BloodGlucoseUnit.Mmoldl)
            val filterSetUiState = awaitItem()
            val flowRecords = filterSetUiState.recordsState as BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords
            expectThat(flowRecords.records[0].value).isEqualTo("1")

            val recordsTranslate = viewModel.uiState.value.recordsState as BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords
            expectThat(recordsTranslate.records[0].value).isEqualTo("0.0555")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun whenAverageEmittedFromTheRepo_ExpectThatUiStateIsUpdatedWithTheTheNewAverage() = runTest{
        //arrange
        var bloodGlucoseRepositoryMock = mock(BloodGlucoseRepository::class.java)
        val testFlow = MutableStateFlow<List<BloodGlucoseRecordModel>>(listOf())
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseRecords).thenReturn(testFlow)
        val bloodGlucoseAverageFlowMock = MutableStateFlow<Float>(0f)
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseAverage).thenReturn(bloodGlucoseAverageFlowMock)

        var viewModel = BloodGlucoseRecordsViewModel(
            bloodGlucoseRepositoryMock,
            ConvertToCurrentLanguageDateFormatUseCaseImpl(),
            ConvertToCurrentLanguageFormatUseCaseImpl(),
            ConvertBloodGlucoseUnitUseCaseUseCaseImpl(
                ConvertMgDlToMmollUseCaseImpl(),
                ConverMmollToMgDlUseCaseImpl(),
                ParseFromCurrentLanguageFormatUseCaseImpl()
            ),
            ValidateGlucoseInputUseCaseImpl(SanitizeDecimalNumberUseCaseImpl())
        )

        //act
        viewModel.onViewReady()

        //assert
        viewModel.uiState.test {
            awaitItem()
            bloodGlucoseAverageFlowMock.value = 1f
            testFlow.value = listOf(BloodGlucoseRecordModel(1f, Date()))
            skipItems(2)
            expectThat(viewModel.uiState.value.average).isEqualTo("1")
            expectNoEvents()
        }
    }
}