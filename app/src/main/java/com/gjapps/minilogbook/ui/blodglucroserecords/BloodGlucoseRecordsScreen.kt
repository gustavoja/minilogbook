package com.gjapps.minilogbook.ui.blodglucroserecords

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
        Box(modifier = modifier
            .padding(top=innerPadding.calculateTopPadding())
            .fillMaxSize()){
            when (state) {
                is BloodGlucoseRecordsUiState.Records ->
                    BloodGlucoseRecordsMenu(
                        inputValue = state.inputValue,
                        selectedFilter = state.selectedUnit,
                        onSave = onSave,
                        onInputValueChanged = onInputValueChanged,
                        onFilterChanged = onFilterChanged,
                        innerPadding=innerPadding,
                        expandedOnAppear = !state.records.any(),
                        modifier = Modifier.align(Alignment.BottomCenter))

                is BloodGlucoseRecordsUiState.Empty -> TODO()
                BloodGlucoseRecordsUiState.Loading -> TODO()
            }
        }
}
}

@Composable
@Preview
fun BloodGlucoseRecordsScreenPreview(){
    MiniLogbookTheme {
        Surface {
            BloodGlucoseRecordsScreen(state = BloodGlucoseRecordsUiState.Records("",
                BloodGlucoseUnit.Mmoldl, emptyList()), {}, {}, {}, Modifier)
        }
    }
}

@Composable
@Preview
fun BloodGlucoseRecordsWithRecordsScreenPreview(){
    MiniLogbookTheme {
        Surface {
            BloodGlucoseRecordsScreen(state = BloodGlucoseRecordsUiState.Records("",
                BloodGlucoseUnit.Mmoldl, listOf("test")
            ), {}, {}, {}, Modifier)
        }
    }
}
