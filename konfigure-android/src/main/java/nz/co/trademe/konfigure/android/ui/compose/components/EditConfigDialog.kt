package nz.co.trademe.konfigure.android.ui.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import nz.co.trademe.konfigure.android.R
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel

@Composable
fun EditConfigDialog(
    item: ConfigAdapterModel,
    inputError: String? = null,
    onDismissRequest: () -> Unit,
    onSave: (String) -> Unit
) {
    var textFieldValue by remember {
        val currentValue = when (item) {
            is ConfigAdapterModel.NumberConfig<*> -> item.value.toString()
            is ConfigAdapterModel.StringConfig -> item.value
            else -> throw UnsupportedOperationException("Editing via input dialog not supported for item type: ${item.javaClass.simpleName}")
        }
        mutableStateOf(currentValue)
    }

    val keyboardType = when (item) {
        is ConfigAdapterModel.NumberConfig<*> -> KeyboardType.Number
        else -> KeyboardType.Text
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.widthIn(min = 280.dp, max = 560.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.edit_config),
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    singleLine = true,
                    isError = inputError != null,
                    supportingText = {
                        inputError?.let {
                            Text(text = it)
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            onSave(textFieldValue)
                        },
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            }
        }
    }
}