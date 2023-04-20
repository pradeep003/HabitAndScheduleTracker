package com.ftcoding.habitandscheduletracker.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ftcoding.habitandscheduletracker.data.dao.ScheduleDao
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.relation.HabitWithResetList
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleDatabaseTest {

    private lateinit var scheduleDatabase: ScheduleDatabase
    private lateinit var scheduleDao: ScheduleDao

    @Before
    fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        scheduleDatabase = Room.inMemoryDatabaseBuilder(context, ScheduleDatabase::class.java).build()
        scheduleDao = scheduleDatabase.scheduleDao
    }

    @After
    fun tearDown() {
        scheduleDatabase.close()
    }

    @Test
    fun addEvent_Assert_IsEventInList() = runTest {
        val event = Event(1, "Coding", "", 1, LocalTime.of(10, 10), LocalTime.of(11,11), "null", listOf(2,3), true)
        scheduleDao.insetScheduleEvent(event)

        val eventList = scheduleDao.getAllSchedules().first()

        Truth.assertThat(eventList).contains(event)
    }

    @Test
    fun addEvent_And_UpdateEvent_Assert_IsUpdatedNameRight() = runTest {
        val event = Event(1, "Coding", "", 1, LocalTime.of(10, 10), LocalTime.of(11,11), "null", listOf(2,3), false)
        scheduleDao.insetScheduleEvent(event)

        val updateEvent = Event(1, "Coding", "", 1, LocalTime.of(10, 10), LocalTime.of(11,11), "null", listOf(2,3), false)
        scheduleDao.insetScheduleEvent(updateEvent)

        val eventList = scheduleDao.getAllSchedules().first()

        Truth.assertThat(eventList).contains(updateEvent)
    }


    @Test
    fun addEvent_And_DeleteEvent_Assert_IsEventNotInList() = runTest {
        val event = Event(1, "Coding", "", 1, LocalTime.of(10, 10), LocalTime.of(11,11), "null", listOf(2,3), true)
        scheduleDao.insetScheduleEvent(event)

        scheduleDao.deleteScheduleEvent(event)

        val eventList = scheduleDao.getAllSchedules().first()

        Truth.assertThat(eventList).doesNotContain(event)
    }

}