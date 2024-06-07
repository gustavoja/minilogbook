package com.gjapps.minilogbook.ui.blodglucroserecords.components.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gjapps.minilogbook.R
import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.ui.sharedcomponents.ValueInput
import com.gjapps.minilogbook.ui.theme.MiniLogbookTheme

@Composable
fun BloodGlucoseRecordsMenu(inputValue:String, selectedFilter: BloodGlucoseUnit, onSave: () -> Unit, onInputValueChanged: (String) -> Unit, onFilterChanged: (BloodGlucoseUnit) -> Unit,
                            innerPadding: PaddingValues,
                            modifier: Modifier = Modifier,
                            expandedOnAppear: Boolean = true)
{
    val inputLabel = if (selectedFilter == BloodGlucoseUnit.Mgdl) stringResource(R.string.mg_dl) else stringResource(
        R.string.mmol_l)

    var addMenuVisible by remember { mutableStateOf(expandedOnAppear) }
    val iconRotation by animateFloatAsState(if(addMenuVisible) 180f else 0f, label = "Blood glucose Menu visibility animation")

    Card(modifier = modifier.fillMaxWidth(),shape = RoundedCornerShape(16.dp,16.dp)) {
        Column(Modifier.fillMaxWidth().padding(bottom = innerPadding.calculateBottomPadding() + 30.dp, top = 10.dp, start = 20.dp, end = 20.dp),horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(
                onClick = {
                    addMenuVisible = !addMenuVisible
                },
                modifier = Modifier.padding(bottom = 10.dp).rotate(iconRotation).align(Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Filled.KeyboardArrowUp , stringResource(R.string.more_options), Modifier.size(30.dp))
            }

            BloodGlucoseUnitHorizontalSelector(selectedFilter, onFilterChanged)
            AnimatedVisibility(addMenuVisible) {
                ValueInput(
                    inputValue,
                    inputLabel,
                    onInputValueChanged,
                    onSave,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }
    }
}

@Composable
@Preview
fun AddBloodGlucoseRecordComponentPreview(){
    MiniLogbookTheme {
        Surface {
            BloodGlucoseRecordsMenu("", BloodGlucoseUnit.Mgdl, {}, {}, {},
                PaddingValues(0.dp),Modifier,true
            )
        }
    }
}
