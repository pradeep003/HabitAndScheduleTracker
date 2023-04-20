package com.ftcoding.habitandscheduletracker.presentation.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


// pop up dialog to show user about permission status
@Composable
fun PermissionDialog (
    context: Context,
    icon: ImageVector,
    message: String,
    closeDialogState: ()-> Unit
) {

    val openDialog = remember {
        mutableStateOf(true)
    }

    if (openDialog.value) {
        Dialog(onDismissRequest = {
            openDialog.value = false
            closeDialogState()
        }) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large
                    ).padding(horizontal = 20.dp),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                 Icon(imageVector = icon, contentDescription = "display icon", tint = MaterialTheme.colorScheme.error, modifier = Modifier
                     .align(CenterHorizontally)
                     .padding(20.dp)
                     .size(50.dp))

                Text(text = message, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                }) {
                        Text(text = "Go to setting", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onError)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
    }
}}
