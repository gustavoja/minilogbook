package com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gjapps.minilogbook.R
import com.gjapps.minilogbook.ui.theme.MiniLogbookTheme

@Composable
fun BloodGlucoseRecordsListMessage(message: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .padding(20.dp))
    {
        Text(text = message,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
@Preview
fun BloodRecordsListMessagePreview(){
    MiniLogbookTheme {
        Surface {
            BloodGlucoseRecordsListMessage(stringResource(R.string.empty_blood_glucose_records))
        }
    }
}
