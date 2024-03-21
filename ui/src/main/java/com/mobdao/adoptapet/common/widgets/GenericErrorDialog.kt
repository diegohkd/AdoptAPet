package com.mobdao.adoptapet.common.widgets

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GenericErrorDialog(onDismissGenericErrorDialog: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissGenericErrorDialog,
        confirmButton = {
            Button(onClick = onDismissGenericErrorDialog) {
                Text(text = "Ok") // TODO do not hardcode
            }
        },
        text = {
            Text(text = "Oops, something went wrong.") // TODO do not hardcode
        }
    )
}