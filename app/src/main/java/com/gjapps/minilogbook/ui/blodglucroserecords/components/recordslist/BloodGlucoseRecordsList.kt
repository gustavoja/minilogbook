package com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gjapps.minilogbook.ui.blodglucroserecords.components.recordslist.uistates.BloodGlucoseRecordItemUIState
import com.gjapps.minilogbook.ui.theme.MiniLogbookTheme


@Composable
fun BloodGlucoseRecordsList(list: List<BloodGlucoseRecordItemUIState>, unitName: String, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    Box(modifier = modifier.fillMaxSize().padding(20.dp))
    {
        LazyColumn(state = listState,modifier = Modifier.fillMaxSize()){
            items(list) { item ->
                ListItem(
                    headlineContent = {
                        Text(text = item.value, modifier = Modifier.align(Alignment.Center))
                    },
                    trailingContent = {
                        Text(text = item.date, modifier = Modifier.align(Alignment.Center))
                    },
                    overlineContent = {
                        Text(text = unitName, modifier = Modifier.align(Alignment.Center))
                    },
                    modifier = Modifier.fillParentMaxWidth())
            }
        }
    }
}

@Composable
@Preview
fun RecordListPreview(){
    MiniLogbookTheme {
        Surface {
            BloodGlucoseRecordsList(listOf(BloodGlucoseRecordItemUIState("150,5","12/12/24")),"unit")
        }
    }
}
