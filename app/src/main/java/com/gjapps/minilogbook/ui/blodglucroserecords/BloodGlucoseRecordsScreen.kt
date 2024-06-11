package com.gjapps.minilogbook.ui.blodglucroserecords

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gjapps.minilogbook.R
import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.ui.blodglucroserecords.components.menu.BloodGlucoseRecordsMenu
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.BloodGlucoseRecordsList
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.BloodGlucoseRecordsListMessage
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.uistates.BloodGlucoseRecordItemUIState
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.uistates.BloodGlucoseRecordsListUIState
import com.gjapps.minilogbook.ui.blodglucroserecords.components.topbar.BloodGlucoseTopBar
import com.gjapps.minilogbook.ui.theme.MiniLogbookTheme


@Composable
fun BloodGlucoseRecordsScreen(
    modifier: Modifier = Modifier,
    viewModel: BloodGlucoseRecordsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)

    LaunchedEffect(Unit) {
        viewModel.onViewReady()
    }
    BloodGlucoseRecordsScreen(state, viewModel::onFilterChanged, viewModel::onNewRecordValueChanged, viewModel::onSaveRecordValue,viewModel::onDeletedRecords,modifier)
}

@Composable
fun BloodGlucoseRecordsScreen(
    state: BloodGlucoseRecordsUiState,
    onFilterChanged: (BloodGlucoseUnit) -> Unit,
    onInputValueChanged: (String) -> Unit,
    onSave: () -> Unit,
    onDeletedRecords: () -> Unit,
    modifier: Modifier = Modifier
)
{
    val selectedUnitName = if (state.selectedUnit == BloodGlucoseUnit.Mgdl) stringResource(R.string.mg_dl) else stringResource(
        R.string.mmol_l)

    Scaffold { innerPadding ->
        Column(modifier = modifier
            .padding(
                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
            )
            .fillMaxSize()){

            BloodGlucoseTopBar(state.average, selectedUnitName,innerPadding, onDeletedRecords, Modifier.align(Alignment.CenterHorizontally))

            when (state.recordsState) {
                BloodGlucoseRecordsListUIState.Empty -> {
                    BloodGlucoseRecordsListMessage(stringResource(id = R.string.empty_blood_glucose_records),Modifier.weight(1f))
                }
                is BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords -> {
                    BloodGlucoseRecordsList(state.recordsState.records, selectedUnitName, Modifier.weight(1f))
                }
                BloodGlucoseRecordsListUIState.Error -> {
                    BloodGlucoseRecordsListMessage(stringResource(R.string.something_went_wrong_while_loading_your_records),Modifier.weight(1f))
                }
                BloodGlucoseRecordsListUIState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize().weight(1f)) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }

            BloodGlucoseRecordsMenu(
                inputValue = state.newRecordUserInputValue,
                selectedUnit = state.selectedUnit,
                isValidValue = state.isValidNewRecordInput,
                selectedUnitName =selectedUnitName,
                onSave = onSave,
                onInputValueChanged = onInputValueChanged,
                onFilterChanged = onFilterChanged,
                innerPadding = innerPadding,
                expandedOnAppear = state.recordsState !is BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords,
                modifier = Modifier
            )
        }
}
}

@Composable
@Preview
fun BloodGlucoseRecordsScreenPreview(){
    MiniLogbookTheme {
        Surface {
            BloodGlucoseRecordsScreen(state = BloodGlucoseRecordsUiState("","",false,
                BloodGlucoseUnit.Mmoldl), {}, {}, {}, {}, Modifier)
        }
    }
}

@Composable
@Preview
fun BloodGlucoseRecordsWithRecordsScreenPreview(){
    MiniLogbookTheme {
        Surface {
            BloodGlucoseRecordsScreen(state = BloodGlucoseRecordsUiState("150","mmol/L",false,
                BloodGlucoseUnit.Mmoldl,recordsState = BloodGlucoseRecordsListUIState.WithBloodGlucoseRecords(listOf(
                    BloodGlucoseRecordItemUIState("150.5","12/12/24",)
                ))), {}, {}, {}, {}, Modifier)
        }
    }
}
