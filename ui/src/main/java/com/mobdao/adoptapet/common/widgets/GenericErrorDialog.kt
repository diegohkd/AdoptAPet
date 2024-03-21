package com.mobdao.adoptapet.common.widgets

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mobdao.adoptapet.R

@Composable
fun GenericErrorDialog(onDismissGenericErrorDialog: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissGenericErrorDialog,
        confirmButton = {
            Button(onClick = onDismissGenericErrorDialog) {
                Text(text = stringResource(R.string.ok))
            }
        },
        text = {
            Text(text = stringResource(R.string.ops_something_went_wrong))
        }
    )
}