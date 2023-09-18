package com.moment.studywithme.ui.screens.home.pomodoro

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteTaskDialog (
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    taskName: String
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        icon = {
            Icon(Icons.Rounded.Delete, contentDescription = "Close icon")
        },
        title = {
            Text(text = "Are you sure?")
        },
        text = {
            Text(text = "You will delete this task permanently \n- $taskName")
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}