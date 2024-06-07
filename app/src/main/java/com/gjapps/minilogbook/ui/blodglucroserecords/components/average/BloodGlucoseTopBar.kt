package com.gjapps.minilogbook.ui.blodglucroserecords.components.average

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    modifier: Modifier = Modifier
) {
    val formattedString = String.format(
        stringResource(R.string.your_average_is),
        average,
        selectedUnitName)

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
            Text(stringResource(R.string.blood_glucose_records),Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleLarge)

            if(!average.isEmpty())
                Text(text = formattedString, style = MaterialTheme.typography.titleMedium, modifier =  Modifier.padding(top = 20.dp))
        }
    }
}

@Composable
@Preview
fun BloodGlucoseAveragePreview(){
    MiniLogbookTheme {
        Surface {
            BloodGlucoseTopBar("150.4", "Mmoldl", PaddingValues(0.dp))
        }
    }
}
