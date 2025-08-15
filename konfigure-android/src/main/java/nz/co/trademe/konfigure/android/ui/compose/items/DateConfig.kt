package nz.co.trademe.konfigure.android.ui.compose.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nz.co.trademe.konfigure.android.R
import nz.co.trademe.konfigure.android.ui.compose.theme.KonfigureTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val MED_DATE_FORMAT = "dd MMM yyyy"
private const val TIME_FORMAT = "h:mma"

@Composable
fun DateConfig(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    value: Date,
    isModified: Boolean,
    onDateClick: () -> Unit,
    onTimeClick:() -> Unit,
    onTodayClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 8.dp,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (isModified) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                        )
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
        )
        Row(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InputChip(
                selected = true,
                onClick = onDateClick,
                label = {
                    Text(text = "${value.format(MED_DATE_FORMAT)}")
                }
            )
            InputChip(
                selected = true,
                onClick = onTimeClick,
                label = {
                    Text(text = "${value.format(TIME_FORMAT)}")
                }
            )
            SuggestionChip(
                onClick = onTodayClick,
                label = {
                    Text(text = stringResource(id = R.string.today))
                },
                shape = RoundedCornerShape(percent = 50)
            )
        }
    }
}

private fun Date.format(format: String): String? {
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(this)
}

@Preview(showBackground = true)
@Composable
private fun DateConfigPreview() {
    KonfigureTheme {
        DateConfig(
            title = "Title",
            description = "Description",
            value = Date(),
            isModified = true,
            onDateClick = {},
            onTimeClick = {},
            onTodayClick = {},
        )
    }
}