package com.ftcoding.habitandscheduletracker.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ftcoding.habitandscheduletracker.presentation.util.state.MessageBarState
import java.util.*
import kotlin.concurrent.schedule

@Composable
fun MessageBar(
    messageBarState: MessageBarState
) {

    var showMessageBar by remember {
        mutableStateOf(false)
    }

    DisposableEffect(key1 = messageBarState.updated) {
        showMessageBar = true
        val timer = Timer("Animation Timer", true)
        timer.schedule(3000L) {
            showMessageBar = false
        }
        onDispose {
            timer.cancel()
            timer.purge()
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {

        AnimatedVisibility(
            visible = messageBarState.error != null && showMessageBar
                    || messageBarState.success != null && showMessageBar,
            enter = expandVertically(
                animationSpec = tween(durationMillis = 300),
                expandFrom = Alignment.Top
            ),
            exit = shrinkVertically(
                animationSpec = tween(durationMillis = 300),
                shrinkTowards = Alignment.Top
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (messageBarState.error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
                    )
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Icon(
                    imageVector = if (messageBarState.success != null) Icons.Filled.Done else Icons.Filled.Error,
                    contentDescription = "message bar state icon",
                    tint = if (messageBarState.success != null) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onError,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = messageBarState.success ?: (messageBarState.error ?: "Unknown error"),
                    color = if (messageBarState.error != null) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }

}


//@Preview(showBackground = true)
//@Composable
//fun MessageBarPreview() {
//
//    val state = MessageBarState()
//    state.addError("Something went wrong")
//    MessageBar(messageBarState = state)
//}