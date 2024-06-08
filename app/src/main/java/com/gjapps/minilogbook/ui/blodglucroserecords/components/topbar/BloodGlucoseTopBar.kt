package com.gjapps.minilogbook.ui.blodglucroserecords.components.topbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gjapps.minilogbook.R
import com.gjapps.minilogbook.ui.theme.MiniLogbookTheme

@Composable
fun BloodGlucoseTopBar(
    average: String,
    selectedUnitName: String,
    innerPadding: PaddingValues,
    onDeletedRecords: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedString = String.format(
        stringResource(R.string.your_average_is),
        average,
        selectedUnitName)

    var displayDialog = remember {
        mutableStateOf(false)
    }

    Card(shape = RectangleShape, modifier = modifier
        .fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(containerColor =  MaterialTheme.colorScheme.tertiaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)){
        Column (
            modifier = Modifier.padding(
                30.dp,
                20.dp + innerPadding.calculateTopPadding(),
                30.dp,
                30.dp)){
            Text(
                stringResource(R.string.blood_glucose_records),
                Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
            if(average.isNotEmpty() && average != "0.0") {
                Row(Modifier.fillMaxWidth().padding(top = 30.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formattedString,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                    )
                    IconButton(onClick = {
                        displayDialog.value = true
                    }, modifier = Modifier.align(Alignment.CenterVertically)) {
                        Icon(Icons.Filled.Delete, "deleteIcon")
                    }

                    if (displayDialog.value)
                        AlertDialog(displayDialog, onDeletedRecords)
                }
            }
        }
    }
}

@Composable
private fun AlertDialog(displayDialog: MutableState<Boolean>, action: () -> Unit) {
    AlertDialog(title = {
        Text(text = "Warning")
    }, text = {
        Text(text = "You're about to delete all your blood glucose records. Do you want to continue?")
    }, onDismissRequest = {
        displayDialog.value = false
    },
        confirmButton = {
            Button(onClick = {
                displayDialog.value = false
                action.invoke()
            }) {
                Text(text = "Delete all")
            }
        }, dismissButton = {
            Button(onClick = {
                displayDialog.value = false
            }) {
                Text(text = "Not now")
            }
        })
}

@Composable
@Preview
fun BloodGlucoseAveragePreview(){
    MiniLogbookTheme {
        Surface {
            BloodGlucoseTopBar("150.4", "Mmoldl",PaddingValues(0.dp),{})
        }
    }
}
