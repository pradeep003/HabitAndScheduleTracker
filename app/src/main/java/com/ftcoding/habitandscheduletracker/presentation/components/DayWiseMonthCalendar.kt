package com.ftcoding.habitandscheduletracker.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DayWiseCalendar(
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
                .padding(bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
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


            // open the lazy row at current date position
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = rememberLazyListState(initialFirstVisibleItemIndex = currentDate.value.dayOfMonth -1)
            ) {
                items(currentDate.value.lengthOfMonth()) { date ->

                    // gets localDate for current lazy column item
                    val currentLocalDate =
                        LocalDate.of(currentDate.value.year, currentDate.value.month, date + 1)

                    Spacer(modifier = Modifier.width(4.dp))
                    Card(
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .padding(2.dp)
                            .clickable {
                                selectedDate.value = currentLocalDate
                                newSelectedDate(currentLocalDate)
                            },
                        shape = MaterialTheme.shapes.extraLarge
                        ,
                        colors = CardDefaults.cardColors(
                            containerColor = if (currentLocalDate.equals(
                                    selectedDate.value
                                )
                            ) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
                        ))
                    {
                        Column(
                            modifier = Modifier
                                .clickable {
                                    selectedDate.value = currentLocalDate
                                    newSelectedDate(currentLocalDate)
                                }
                                .padding(10.dp),

                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            // textview for dayOfWeek
                            Text(
                                text = currentLocalDate.dayOfWeek.name.substring(0..2),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (currentLocalDate != selectedDate.value) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary
                            )
                            // textview for date
                            Text(
                                text = "${date + 1}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = if (currentLocalDate != selectedDate.value) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary
                            )

                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DayWiseMonthCalendarPreview() {
    DayWiseCalendar(date = LocalDate.now()) {

    }
}