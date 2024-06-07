package com.gjapps.minilogbook.ui.blodglucroserecords

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gjapps.minilogbook.R
import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.ui.blodglucroserecords.components.average.BloodGlucoseTopBar
import com.gjapps.minilogbook.ui.blodglucroserecords.components.menu.BloodGlucoseRecordsMenu
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.RecordListEmptyState
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.RecordsList
import com.gjapps.minilogbook.ui.theme.MiniLogbookTheme


@Composable
fun BloodGlucoseRecordsScreen(
    modifier: Modifier = Modifier,
    viewModel: BloodGlucoseRecordsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
    BloodGlucoseRecordsScreen(state, viewModel::onFilterChanged, viewModel::onRecordValueChanged, viewModel::onSaveRecordValue,modifier)
}

@Composable
fun BloodGlucoseRecordsScreen(
    state: BloodGlucoseRecordsUiState,
    onFilterChanged: (BloodGlucoseUnit) -> Unit,
    onInputValueChanged: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
)
{
    val selectedUnitName = if (state.selectedUnit == BloodGlucoseUnit.Mgdl) stringResource(R.string.mg_dl) else stringResource(
        R.string.mmol_l)

    Scaffold { innerPadding ->
        Column(modifier = modifier
            .padding()
            .fillMaxSize()){

            BloodGlucoseTopBar(state.average, selectedUnitName,innerPadding, Modifier.align(Alignment.CenterHorizontally))

            when (state.recordsState) {
                RecordsState.Empty -> {
                    RecordListEmptyState(Modifier.weight(1f))
                }
                is RecordsState.WithRecords -> {
                    RecordsList(state.recordsState.records,Modifier.weight(1f))
                }
            }

            BloodGlucoseRecordsMenu(
                inputValue = state.newRecordInputValue,
                selectedUnit = state.selectedUnit,
                selectedUnitName =selectedUnitName,
                onSave = onSave,
                onInputValueChanged = onInputValueChanged,
                onFilterChanged = onFilterChanged,
                innerPadding = innerPadding,
                expandedOnAppear = state.recordsState !is RecordsState.WithRecords,
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
            BloodGlucoseRecordsScreen(state = BloodGlucoseRecordsUiState("","",
                BloodGlucoseUnit.Mmoldl), {}, {}, {}, Modifier)
        }
    }
}

@Composable
@Preview
fun BloodGlucoseRecordsWithRecordsScreenPreview(){
    MiniLogbookTheme {
        Surface {
            BloodGlucoseRecordsScreen(state = BloodGlucoseRecordsUiState("150","mmol/L",
                BloodGlucoseUnit.Mmoldl,recordsState = RecordsState.WithRecords(listOf("record"))), {}, {}, {}, Modifier)
        }
    }
}
