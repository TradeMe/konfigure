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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nz.co.trademe.konfigure.android.R
import nz.co.trademe.konfigure.android.ui.compose.theme.KonfigureTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val MED_DATE_FORMAT = "dd MMM yyyy"
private const val TIME_FORMAT = "h:mma"

private enum class DatePickerType {
    Date,
    Time,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DateConfig(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    value: Date,
    isModified: Boolean,
    onConfigChange: (Date) -> Unit,
) {
    var activeDatePicker by remember { mutableStateOf<DatePickerType?>(null) }

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
                onClick = { activeDatePicker = DatePickerType.Date },
                label = {
                    Text(text = "${value.format(MED_DATE_FORMAT)}")
                }
            )
            InputChip(
                selected = true,
                onClick = { activeDatePicker = DatePickerType.Time },
                label = {
                    Text(text = "${value.format(TIME_FORMAT)}")
                }
            )
            SuggestionChip(
                onClick = {
                    val calendar = Calendar.getInstance()
                    onConfigChange(calendar.time)
                },
                label = {
                    Text(text = stringResource(id = R.string.today))
                },
                shape = RoundedCornerShape(percent = 50)
            )
        }
    }

    if (activeDatePicker == DatePickerType.Date) {
        DatePickerDialog(
            initialValue = value,
            onDateSelected = { selectedDate ->
                selectedDate?.let {
                    val selectedDate = Date(it)
                    val calendar = Calendar.getInstance().apply { time = value }
                    val selectedCalendar = Calendar.getInstance().apply { time = selectedDate }
                    calendar.set(
                        selectedCalendar.get(Calendar.YEAR),
                        selectedCalendar.get(Calendar.MONTH),
                        selectedCalendar.get(Calendar.DAY_OF_MONTH)
                    )

                    onConfigChange(calendar.time)
                    activeDatePicker = null
                }
            },
            onDismiss = { activeDatePicker = null }
        )
    } else if (activeDatePicker == DatePickerType.Time) {
        val calendar = Calendar.getInstance().apply { time = value }
        val timePickerState = rememberTimePickerState(
            initialHour = calendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = calendar.get(Calendar.MINUTE),
        )

        TimePickerDialog(
            onDismiss = { activeDatePicker = null },
            onConfirm = {
                val newCalendar = Calendar.getInstance().apply { time = value }
                newCalendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                newCalendar.set(Calendar.MINUTE, timePickerState.minute)

                onConfigChange(newCalendar.time)
                activeDatePicker = null
            }
        ) {
            TimePicker(
                state = timePickerState,
            )
        }
    }
}

private fun Date.format(format: String): String? {
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(this)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    initialValue: Date,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialValue.time
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.dialog_button_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_button_dismiss))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
private fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.dialog_button_dismiss))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(stringResource(R.string.dialog_button_confirm))
            }
        },
        text = { content() }
    )
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
            onConfigChange = {}
        )
    }
}