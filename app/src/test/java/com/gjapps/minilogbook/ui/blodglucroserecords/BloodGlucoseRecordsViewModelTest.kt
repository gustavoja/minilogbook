package com.gjapps.minilogbook.ui.blodglucroserecords

import app.cash.turbine.test
import com.gjapps.minilogbook.data.models.BloodGlucoseRecordModel
import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.data.repositories.BloodGlucoseRepository
import com.gjapps.minilogbook.domain.usecases.ApplyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertBloodGlucoseUnitUseCaseUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertMgDlToMmollUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertMmollToMgDlUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageDateFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ConvertToCurrentLanguageFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.DecimalSeparatorForCurrentLocaleUseCase
import com.gjapps.minilogbook.domain.usecases.GetBloodGlucoseAverageUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.GetLocalisedBloodGlucoseRecordsUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.ParseFromLanguageFormatUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.SanitizeDecimalNumberUseCaseImpl
import com.gjapps.minilogbook.domain.usecases.SaveRecordUseCaseImpl
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
import org.mockito.kotlin.doReturn
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
    private lateinit var decimalSeparatorForCurrentLocaleUseCase: DecimalSeparatorForCurrentLocaleUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        bloodGlucoseRepositoryMock = setupBloodRecordsRepositoryMock()

        createViewModel()
    }

    private fun createViewModel() {

        decimalSeparatorForCurrentLocaleUseCase = mock {
            on { invoke() } doReturn '.' // Customize the decimal separator as needed
        }

        val convertBloodGlucoseUnitUseCaseUseCaseImpl =  ConvertBloodGlucoseUnitUseCaseUseCaseImpl(
        ConvertMgDlToMmollUseCaseImpl(),
        ConvertMmollToMgDlUseCaseImpl(),
        ParseFromLanguageFormatUseCaseImpl()
        )

        val applyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl = ApplyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl(ConvertToCurrentLanguageFormatUseCaseImpl(),convertBloodGlucoseUnitUseCaseUseCaseImpl)
        viewModel = BloodGlucoseRecordsViewModel(
            bloodGlucoseRepositoryMock,
            ValidateGlucoseInputUseCaseImpl(SanitizeDecimalNumberUseCaseImpl(
                decimalSeparatorForCurrentLocaleUseCase),
                decimalSeparatorForCurrentLocaleUseCase
            ),
            GetLocalisedBloodGlucoseRecordsUseCaseImpl(bloodGlucoseRepositoryMock,
                ConvertToCurrentLanguageDateFormatUseCaseImpl(),
                ConvertToCurrentLanguageFormatUseCaseImpl(),
                convertBloodGlucoseUnitUseCaseUseCaseImpl),
            GetBloodGlucoseAverageUseCaseImpl(bloodGlucoseRepositoryMock,
                ConvertToCurrentLanguageFormatUseCaseImpl(),
                convertBloodGlucoseUnitUseCaseUseCaseImpl),
            SaveRecordUseCaseImpl(bloodGlucoseRepositoryMock,convertBloodGlucoseUnitUseCaseUseCaseImpl),
            applyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl
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
            expectThat(item.newRecordUserInputValue).isEqualTo("")
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
            expectThat(item.newRecordUserInputValue).isEqualTo("1")
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
            expectThat(item.newRecordUserInputValue).isEqualTo("1")
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

        decimalSeparatorForCurrentLocaleUseCase = mock {
            on { invoke() } doReturn '.'
        }

        val convertBloodGlucoseUnitUseCaseUseCaseImpl =  ConvertBloodGlucoseUnitUseCaseUseCaseImpl(
            ConvertMgDlToMmollUseCaseImpl(),
            ConvertMmollToMgDlUseCaseImpl(),
            ParseFromLanguageFormatUseCaseImpl()
        )

        val applyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl = ApplyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl(ConvertToCurrentLanguageFormatUseCaseImpl(),convertBloodGlucoseUnitUseCaseUseCaseImpl)
        viewModel = BloodGlucoseRecordsViewModel(
            bloodGlucoseRepositoryMock,
            ValidateGlucoseInputUseCaseImpl(SanitizeDecimalNumberUseCaseImpl(
                decimalSeparatorForCurrentLocaleUseCase),
                decimalSeparatorForCurrentLocaleUseCase
            ),
            GetLocalisedBloodGlucoseRecordsUseCaseImpl(bloodGlucoseRepositoryMock,
                ConvertToCurrentLanguageDateFormatUseCaseImpl(),
                ConvertToCurrentLanguageFormatUseCaseImpl(),
                convertBloodGlucoseUnitUseCaseUseCaseImpl),
            GetBloodGlucoseAverageUseCaseImpl(bloodGlucoseRepositoryMock,
                ConvertToCurrentLanguageFormatUseCaseImpl(),
                convertBloodGlucoseUnitUseCaseUseCaseImpl),
            SaveRecordUseCaseImpl(bloodGlucoseRepositoryMock,convertBloodGlucoseUnitUseCaseUseCaseImpl),
            applyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl
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
    fun whenRecordListEmittedFromTheRepo_ExpectThatUiStateIsUpdatedWithTheTasksEmitted() = runTest{
        //arrange
        val bloodGlucoseRepositoryMock = mock(BloodGlucoseRepository::class.java)
        val testFlow = MutableStateFlow<List<BloodGlucoseRecordModel>>(listOf())
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseRecords).thenReturn(testFlow)
        val bloodGlucoseAverageFlowMock = mock<Flow<Float>>()
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseAverage).thenReturn(bloodGlucoseAverageFlowMock)

        val convertBloodGlucoseUnitUseCaseUseCaseImpl =  ConvertBloodGlucoseUnitUseCaseUseCaseImpl(
            ConvertMgDlToMmollUseCaseImpl(),
            ConvertMmollToMgDlUseCaseImpl(),
            ParseFromLanguageFormatUseCaseImpl()
        )
        val applyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl = ApplyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl(ConvertToCurrentLanguageFormatUseCaseImpl(),convertBloodGlucoseUnitUseCaseUseCaseImpl)
        viewModel = BloodGlucoseRecordsViewModel(
            bloodGlucoseRepositoryMock,
            ValidateGlucoseInputUseCaseImpl(SanitizeDecimalNumberUseCaseImpl(
                decimalSeparatorForCurrentLocaleUseCase),
                decimalSeparatorForCurrentLocaleUseCase
            ),
            GetLocalisedBloodGlucoseRecordsUseCaseImpl(bloodGlucoseRepositoryMock,
                ConvertToCurrentLanguageDateFormatUseCaseImpl(),
                ConvertToCurrentLanguageFormatUseCaseImpl(),
                convertBloodGlucoseUnitUseCaseUseCaseImpl),
            GetBloodGlucoseAverageUseCaseImpl(bloodGlucoseRepositoryMock,
                ConvertToCurrentLanguageFormatUseCaseImpl(),
                convertBloodGlucoseUnitUseCaseUseCaseImpl),
            SaveRecordUseCaseImpl(bloodGlucoseRepositoryMock,convertBloodGlucoseUnitUseCaseUseCaseImpl),
            applyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl
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
        val bloodGlucoseRepositoryMock = mock(BloodGlucoseRepository::class.java)
        val testFlow = MutableStateFlow<List<BloodGlucoseRecordModel>>(listOf())
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseRecords).thenReturn(testFlow)
        val bloodGlucoseAverageFlowMock = mock<Flow<Float>>()
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseAverage).thenReturn(bloodGlucoseAverageFlowMock)

        val convertBloodGlucoseUnitUseCaseUseCaseImpl =  ConvertBloodGlucoseUnitUseCaseUseCaseImpl(
            ConvertMgDlToMmollUseCaseImpl(),
            ConvertMmollToMgDlUseCaseImpl(),
            ParseFromLanguageFormatUseCaseImpl()
        )
        val applyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl = ApplyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl(ConvertToCurrentLanguageFormatUseCaseImpl(),convertBloodGlucoseUnitUseCaseUseCaseImpl)
        viewModel = BloodGlucoseRecordsViewModel(
            bloodGlucoseRepositoryMock,
            ValidateGlucoseInputUseCaseImpl(SanitizeDecimalNumberUseCaseImpl(
                decimalSeparatorForCurrentLocaleUseCase),
                decimalSeparatorForCurrentLocaleUseCase
            ),
            GetLocalisedBloodGlucoseRecordsUseCaseImpl(bloodGlucoseRepositoryMock,
                ConvertToCurrentLanguageDateFormatUseCaseImpl(),
                ConvertToCurrentLanguageFormatUseCaseImpl(),
                convertBloodGlucoseUnitUseCaseUseCaseImpl),
            GetBloodGlucoseAverageUseCaseImpl(bloodGlucoseRepositoryMock,
                ConvertToCurrentLanguageFormatUseCaseImpl(),
                convertBloodGlucoseUnitUseCaseUseCaseImpl),
            SaveRecordUseCaseImpl(bloodGlucoseRepositoryMock,convertBloodGlucoseUnitUseCaseUseCaseImpl),
            applyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl
        )

        //act
        viewModel.onViewReady()

        //assert
        viewModel.uiState.test {
            awaitItem()
            testFlow.value = listOf(BloodGlucoseRecordModel(1f, Date()))
            awaitItem()
            viewModel.onFilterChanged(BloodGlucoseUnit.Mmoldl)
            awaitItem()
            awaitItem()

            val recordsTranslate = viewModel.uiState.value.recordsState as BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords
            expectThat(recordsTranslate.records[0].value).isEqualTo("0.0555")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun whenAverageEmittedFromTheRepo_ExpectThatUiStateIsUpdatedWithTheTheNewAverage() = runTest{
        //arrange
        val bloodGlucoseRepositoryMock = mock(BloodGlucoseRepository::class.java)
        val testFlow = MutableStateFlow<List<BloodGlucoseRecordModel>>(listOf())
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseRecords).thenReturn(testFlow)
        val bloodGlucoseAverageFlowMock = MutableStateFlow(0f)
        `when`(bloodGlucoseRepositoryMock.bloodGlucoseAverage).thenReturn(bloodGlucoseAverageFlowMock)

        val convertBloodGlucoseUnitUseCaseUseCaseImpl =  ConvertBloodGlucoseUnitUseCaseUseCaseImpl(
            ConvertMgDlToMmollUseCaseImpl(),
            ConvertMmollToMgDlUseCaseImpl(),
            ParseFromLanguageFormatUseCaseImpl()
        )
        val applyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl = ApplyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl(ConvertToCurrentLanguageFormatUseCaseImpl(),convertBloodGlucoseUnitUseCaseUseCaseImpl)
        viewModel = BloodGlucoseRecordsViewModel(
            bloodGlucoseRepositoryMock,
            ValidateGlucoseInputUseCaseImpl(SanitizeDecimalNumberUseCaseImpl(
                decimalSeparatorForCurrentLocaleUseCase),
                decimalSeparatorForCurrentLocaleUseCase
            ),
            GetLocalisedBloodGlucoseRecordsUseCaseImpl(bloodGlucoseRepositoryMock,
                ConvertToCurrentLanguageDateFormatUseCaseImpl(),
                ConvertToCurrentLanguageFormatUseCaseImpl(),
                convertBloodGlucoseUnitUseCaseUseCaseImpl),
            GetBloodGlucoseAverageUseCaseImpl(bloodGlucoseRepositoryMock,
                ConvertToCurrentLanguageFormatUseCaseImpl(),
                convertBloodGlucoseUnitUseCaseUseCaseImpl),
            SaveRecordUseCaseImpl(bloodGlucoseRepositoryMock,convertBloodGlucoseUnitUseCaseUseCaseImpl),
            applyUnitAndLocaliseBloodGlucoseRecordUseCaseImpl
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