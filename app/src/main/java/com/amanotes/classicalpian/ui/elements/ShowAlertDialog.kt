package com.amanotes.classicalpian.ui.elements

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.amanotes.classicalpian.R

@Composable
fun ShowAlertDialog(
    alertTitle: String,
    alertMessage: String,
    onPositiveClick: () -> Unit,
    onDismiss: () -> Unit = {},
    positiveButtonText: Int = R.string.yes,
    negativeButtonText: Int = R.string.no,
    showDismissButton: Boolean = true,
    cancelable: Boolean = true,
    textContentColor: Color = Color.Black,
    titleContentColor: Color = Color.Black,
    containerColor: Color = Color.White
) {

    AlertDialog(
        title = { Text(text = alertTitle) },
        text = { Text(text = alertMessage) },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onPositiveClick() }) {
                Text(text = stringResource(id = positiveButtonText))
            }
        },
        dismissButton = {
            if (showDismissButton) TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = negativeButtonText))
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = cancelable,
            dismissOnClickOutside = cancelable
        ),
        textContentColor = textContentColor,
        titleContentColor = titleContentColor,
        containerColor = containerColor
    )

}