package com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gjapps.minilogbook.ui.theme.MiniLogbookTheme


@Composable
fun RecordsList(list: List<String>, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize())
    {
        Text(text = "Records found", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
@Preview
fun RecordListPreview(){
    MiniLogbookTheme {
        Surface {
            RecordsList(emptyList())
        }
    }
}
