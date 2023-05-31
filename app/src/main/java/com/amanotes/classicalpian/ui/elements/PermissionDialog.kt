package com.amanotes.classicalpian.ui.elements

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.AlertDialogDefaults.textContentColor
import androidx.compose.material3.AlertDialogDefaults.titleContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.amanotes.classicalpian.R

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
    onGoToSettings: () -> Unit = {},
    cancelable: Boolean = true,
) {
    val context = LocalContext.current

    AlertDialog(
        title = { Text(text = stringResource(id = R.string.permission_request)) },
        text = {
            Text(text = permissionTextProvider.getDescription(context, isPermanentlyDeclined))
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            if (isPermanentlyDeclined) {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.ok))
                }
            } else {
                TextButton(onClick = onSubmit) {
                    Text(text = stringResource(id = R.string.grant))
                }
            }
        },
        dismissButton = {
            if (!isPermanentlyDeclined) TextButton(onClick = onCancel) {
                Text(text = stringResource(id = R.string.reject))
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

interface PermissionTextProvider {
    fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String
}

class PostPermissionTextProvider() : PermissionTextProvider {

    override fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) context.getText(R.string.turn_on_manually).toString()
        else context.getText(R.string.app_needs_confirmation).toString()
    }

}