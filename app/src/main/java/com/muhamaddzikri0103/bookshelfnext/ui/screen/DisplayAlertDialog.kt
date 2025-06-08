package com.muhamaddzikri0103.bookshelfnext.ui.screen

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.muhamaddzikri0103.bookshelfnext.ui.theme.BookShelfTheme

@Composable
fun DisplayAlertDialog(
    displayText: String,
    confirmText: String,
    dismissText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        text = { Text(text = displayText) },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = dismissText)
            }
        },
        onDismissRequest = { onDismissRequest() }
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DialogPreview() {
    BookShelfTheme {
        DisplayAlertDialog(
            displayText = "Are you sure?",
            confirmText = "Yes",
            dismissText = "No",
            onDismissRequest = {},
            onConfirmation = {}
        )
    }
}