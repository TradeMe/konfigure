package nz.co.trademe.konfigure.android.ui.compose.items

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nz.co.trademe.konfigure.android.ui.compose.theme.KonfigureTheme

@Composable
fun GroupHeader(
    modifier: Modifier = Modifier,
    name: String,
) {
    Text(
        text = name,
        modifier = modifier.padding(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 8.dp
        ),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.secondary,
    )
}

@Preview(showBackground = true)
@Composable
private fun GroupHeaderPreview() {
    KonfigureTheme {
        GroupHeader(name = "Group header")
    }
}