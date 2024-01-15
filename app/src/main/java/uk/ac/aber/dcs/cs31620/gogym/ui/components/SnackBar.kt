package uk.ac.aber.dcs.cs31620.gogym.ui.components

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * @param data Snack Bar specific data
 * @param onDismiss on dismiss lambda
 */
@Composable
fun SnackBar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    Snackbar(
        modifier = modifier,
        content = {
            Text(
                text = data.visuals.message
            )
        },
        action = {
            data.visuals.actionLabel?.let { actionLabel ->
                TextButton(
                    onClick = { onDismiss() }
                ) {
                    Text(
                        text = actionLabel
                    )
                }
            }
        }
    )
}