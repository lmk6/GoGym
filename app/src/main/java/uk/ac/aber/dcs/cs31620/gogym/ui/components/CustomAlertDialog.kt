package uk.ac.aber.dcs.cs31620.gogym.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import uk.ac.aber.dcs.cs31620.gogym.R

/**
 * Custom Alert Dialog
 * @param titleText Title of the Dialog.
 * @param descriptionText Description text.
 * @param onDismiss on dismiss lambda.
 * @param onConfirm on confirm lambda.
 */
@Composable
fun CustomAlertDialog(
    titleText: String,
    descriptionText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = titleText)
        },
        text = {
            Text(text = descriptionText)
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { onConfirm() }
            ) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}