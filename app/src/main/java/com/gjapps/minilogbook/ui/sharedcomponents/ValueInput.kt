package com.gjapps.minilogbook.ui.sharedcomponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gjapps.minilogbook.R
import com.gjapps.minilogbook.ui.theme.MiniLogbookTheme

@Composable
fun ValueInput(
    inputValue: String,
    label:String,
    onInputValueChanged: (String) -> Unit,
    onSave: () -> Unit,
    icon : ImageVector = Icons.Filled.Add,
    iconDescription: String = stringResource(R.string.add_blood_sugar_record),
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth().padding(0.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = inputValue,
            onValueChange = onInputValueChanged,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            maxLines = 1,
            label = { Text(label) }
        )
        IconButton(
            modifier = Modifier.padding(start = 10.dp),
            enabled = !inputValue.isNullOrEmpty(),
            onClick = { onSave.invoke() }
        ) {
            Icon(imageVector = icon, iconDescription)
        }
    }
}


@Composable
@Preview
fun ValueInputPreview(){
    MiniLogbookTheme {
        Surface {
            ValueInput("","value",{},{})
        }
    }
}

