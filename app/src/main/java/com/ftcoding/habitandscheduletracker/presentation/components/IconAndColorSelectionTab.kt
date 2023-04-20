package com.ftcoding.habitandscheduletracker.presentation.components

import android.util.Log
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.End
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ftcoding.habitandscheduletracker.data.domain.models.data.HabitIcon
import com.ftcoding.habitandscheduletracker.data.domain.models.data.IconAndColor
import com.ftcoding.habitandscheduletracker.util.HabitConstants.HABIT_ICON_LIST

@Composable
fun IconAndColorSelectionTab(
    selectedIconAndColor: IconAndColor = IconAndColor(),
    newSelectedIconAndColor: (IconAndColor) -> Unit,
    closeDialogState: () -> Unit
) {

    var tabState by remember {
        mutableStateOf(0)
    }
    val iconAndColorState by remember {
        mutableStateOf(selectedIconAndColor)
    }
    val title = listOf("Color", "Icons")

    val openDialog = remember {
        mutableStateOf(true)
    }

    if (openDialog.value) {
        Dialog(onDismissRequest = {
            openDialog.value = false
            closeDialogState()
        }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.large
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                    TabRow(
                        selectedTabIndex = tabState,
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .clip(RoundedCornerShape(50.dp))
                    ) {
                        title.forEachIndexed { index, title ->
                            Tab(
                                modifier = if (tabState == index)
                                    Modifier
                                        .padding(3.dp)
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(MaterialTheme.colorScheme.background)
                                else
                                    Modifier
                                        .padding(3.dp)
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(MaterialTheme.colorScheme.primary),
                                selected = tabState == index,
                                onClick = {
                                    tabState = index
                                },
                                text = {
                                    Text(
                                        text = title,
                                        maxLines = 1,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = if (tabState == index) MaterialTheme.colorScheme.primary else Color.White
                                    )
                                },
                                selectedContentColor = MaterialTheme.colorScheme.primary,
                                unselectedContentColor = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    if (tabState == 0) {
                        ColorPicker {
                            iconAndColorState.color = it
                        }
                    } else {
                        IconSelectionTabView {
                            iconAndColorState.icon = it
                        }
                    }

                }

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                ) {

                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                newSelectedIconAndColor(iconAndColorState)
                                openDialog.value = false
                                closeDialogState()
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

        }
    }
}


@Composable
fun IconSelectionTabView(
    selectedIcon: (Int) -> Unit
) {

    val newSelectedIcon = remember {
        mutableStateOf(HABIT_ICON_LIST[0].icon)
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 45.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        items(HABIT_ICON_LIST) { habitIcon ->
            val backgroundColor =
                if (habitIcon.icon == newSelectedIcon.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
            Card(
                modifier = Modifier
                    .clickable {
                        newSelectedIcon.value = habitIcon.icon
                    },
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {

                Icon(
                    painter = painterResource(id = habitIcon.icon),
                    contentDescription = habitIcon.name,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                )
            }
        }
    }
    newSelectedIcon.value.let { selectedIcon(it) }
}


@Preview(showBackground = true)
@Composable
fun IconAndColorSelectionTabPreview() {
    IconAndColorSelectionTab(
        selectedIconAndColor = IconAndColor(),
        closeDialogState = {

        },
        newSelectedIconAndColor = {

        }
    )

}