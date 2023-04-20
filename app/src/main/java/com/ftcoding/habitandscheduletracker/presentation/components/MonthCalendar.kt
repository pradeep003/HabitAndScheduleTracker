package com.ftcoding.habitandscheduletracker.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun MonthCalendar(
    date: LocalDate = LocalDate.now(),
    newSelectedDate : (LocalDate) -> Unit
) {

    // selectedDate == store user selected date
    val selectedDate = rememberSaveable {
        mutableStateOf(date)
    }

    val currentDate = remember {
        mutableStateOf(date)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // shows previous month when user clicks and change selectedValue
                IconButton(onClick = {
                    currentDate.value = currentDate.value.minusMonths(1)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowLeft,
                        contentDescription = "previous icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = "${currentDate.value.month.name} ${currentDate.value.year}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(weight = 1f),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                // shows next month when user clicks and change selectedValue
                IconButton(onClick = {
                    currentDate.value = currentDate.value.plusMonths(1)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowRight,
                        contentDescription = "next icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }


            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.animateContentSize()
            ) {

                items(7) {
                    Text(
                        text = DayOfWeek.of(it + 1).toString().substring(0..2),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(2.dp)
                    )

                }

                // adding dayOfWeek to current month length
                val firstDayOfCurrentMonth =
                    LocalDate.of(currentDate.value.year, currentDate.value.month, 1)
                val firstDayOfWeekOfFirstDay = firstDayOfCurrentMonth.dayOfWeek.value - 1
                val totalDays = currentDate.value.lengthOfMonth() + firstDayOfWeekOfFirstDay


                items(totalDays) { day ->

                    if (day >= (firstDayOfWeekOfFirstDay)) {

                        val currentLocalDate =
                            LocalDate.of(
                                currentDate.value.year,
                                currentDate.value.month,
                                (day - firstDayOfWeekOfFirstDay) + 1
                            )
                        // gets localDate for current lazy column item
                        Card(
                            elevation = CardDefaults.cardElevation(4.dp),
                            modifier = Modifier
                                .padding(2.dp)
                                .clickable {
                                    selectedDate.value = currentLocalDate
                                    newSelectedDate(currentLocalDate)
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (currentLocalDate.equals(
                                        selectedDate.value
                                    )
                                ) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
                            )
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .align(CenterHorizontally)
                            ) {
                                Text(
                                    text = currentLocalDate.dayOfMonth.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = if (currentLocalDate.equals(selectedDate.value)) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthCalendarPreview() {
    MonthCalendar() {

    }
}