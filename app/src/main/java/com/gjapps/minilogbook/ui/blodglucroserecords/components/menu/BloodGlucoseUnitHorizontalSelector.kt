package com.gjapps.minilogbook.ui.blodglucroserecords.components.menu
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gjapps.minilogbook.R
import com.gjapps.minilogbook.data.models.BloodGlucoseUnit
import com.gjapps.minilogbook.ui.theme.MiniLogbookTheme

@Composable
fun BloodGlucoseUnitHorizontalSelector(
    selectedFilter: BloodGlucoseUnit,
    onFilterChanged: (BloodGlucoseUnit) -> Unit
) {
    val animatedOffset = animateFloatAsState(targetValue = if (selectedFilter == BloodGlucoseUnit.Mgdl) 0f else .5f,
        label = "Blood glucose horizontal selector animation"
    )
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(animatedOffset.value)
                    .animateContentSize()
            )
            ElevatedSuggestionChip(
                onClick = {},
                label = {},
                colors =  SuggestionChipDefaults.suggestionChipColors().copy(MaterialTheme.colorScheme.onTertiary),
                modifier = Modifier
                    .fillMaxWidth(.1f)
                    .weight(1f)
                    .fillMaxHeight(),
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(.5f - animatedOffset.value)
                    .animateContentSize()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = stringResource(R.string.mg_dl),
                Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onFilterChanged(BloodGlucoseUnit.Mgdl)
                    }
                    .weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge)
            Text(text = stringResource(R.string.mmol_l),
                Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onFilterChanged(BloodGlucoseUnit.Mmoldl)
                    }
                    .weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
@Preview
fun BloodGlucoseUnitHorizontalSelectorPreview(){
    MiniLogbookTheme {
        Surface {
            BloodGlucoseUnitHorizontalSelector(BloodGlucoseUnit.Mgdl) {}
        }
    }
}