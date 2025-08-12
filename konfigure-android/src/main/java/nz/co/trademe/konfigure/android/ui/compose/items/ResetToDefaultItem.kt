package nz.co.trademe.konfigure.android.ui.compose.items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nz.co.trademe.konfigure.android.R
import nz.co.trademe.konfigure.android.ui.compose.theme.KonfigureTheme

@Composable
fun ResetToDefaultItem(
    modifier: Modifier = Modifier,
    onResetClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.reset_experiments_description),
            modifier = modifier.weight(1f),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
        )
        FilledTonalButton(
            modifier = Modifier
                .defaultMinSize(minHeight = 48.dp)
                .padding(start = 8.dp),
            onClick = onResetClick,
        ) {
            Text(text = stringResource(id = R.string.reset_to_default))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResetToDefaultItemPreview() {
    KonfigureTheme {
        ResetToDefaultItem {}
    }
}