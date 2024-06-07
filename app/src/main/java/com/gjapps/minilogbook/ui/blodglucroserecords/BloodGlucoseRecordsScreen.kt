package com.gjapps.minilogbook.ui.blodglucroserecords

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gjapps.minilogbook.R
import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.ui.blodglucroserecords.components.menu.BloodGlucoseRecordsMenu
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
    Scaffold { innerPadding ->
        Column(modifier = modifier
            .padding(top = innerPadding.calculateTopPadding())
            .fillMaxSize()){

            BloodGlucoseAverage(state.average, state.selectedUnit, Modifier.weight(1f))
            when (state.records) {
                RecordsState.Empty -> {
                    RecordListEmptyState(Modifier.weight(1f))
                }
                is RecordsState.WithRecords -> {
                    RecordList(Modifier.weight(1f))
                }
            }

            BloodGlucoseRecordsMenu(
                inputValue = state.newRecordInputValue,
                selectedFilter = state.selectedUnit,
                onSave = onSave,
                onInputValueChanged = onInputValueChanged,
                onFilterChanged = onFilterChanged,
                innerPadding = innerPadding,
                expandedOnAppear = state.records !is RecordsState.WithRecords,
                modifier = Modifier
            )
        }
}
}

@Composable
fun BloodGlucoseAverage(average: String, selectedUnit: BloodGlucoseUnit, modifier: Modifier) {

    if(average.isEmpty())
        return

    val formattedString = String.format(
        stringResource(R.string.your_average_is),
        average,
        if(selectedUnit == BloodGlucoseUnit.Mmoldl) stringResource(R.string.mmol_l) else stringResource( R.string.mg_dl) )

    Text(text = formattedString, style = MaterialTheme.typography.titleLarge, modifier = modifier)
}

@Composable
fun RecordList(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize())
    {
        Text(text = "Records found", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun RecordListEmptyState(modifier: Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .padding(20.dp))
    {
        Text(text = stringResource(R.string.emoty_blood_glucose_records),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center))
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
            BloodGlucoseRecordsScreen(state = BloodGlucoseRecordsUiState("","",
                BloodGlucoseUnit.Mmoldl,records = RecordsState.WithRecords(listOf("record"))), {}, {}, {}, Modifier)
        }
    }
}
