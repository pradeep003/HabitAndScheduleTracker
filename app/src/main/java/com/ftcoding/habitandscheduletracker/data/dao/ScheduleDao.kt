package com.ftcoding.habitandscheduletracker.data.dao

import androidx.room.*
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Notes
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.ScheduleTrackerTime
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.relation.EventWithNotes
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.relation.EventWithTrackerTimes
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM event")
    fun getAllSchedules(): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE eventId = :eventId")
    suspend fun getScheduleEventById(eventId: Int): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetScheduleEvent(event: Event) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackerTime(data: ScheduleTrackerTime)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(data: Notes)

    @Delete
    suspend fun deleteScheduleEvent(event: Event)

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    fun getScheduleWithTrackerTime(eventId: Int) : List<EventWithTrackerTimes>

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    fun getScheduleWithNotes(eventId: Int): List<EventWithNotes>

}