package com.ftcoding.habitandscheduletracker.data.db

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ftcoding.habitandscheduletracker.data.dao.HabitDao
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class HabitDatabaseTest {

    private lateinit var habitDao: HabitDao
    private lateinit var habitDatabase: HabitDatabase

    @Before
    fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        habitDatabase = Room.inMemoryDatabaseBuilder(context, HabitDatabase::class.java).build()
        habitDao = habitDatabase.habitDao
    }

    @After
    fun tearDown() {
        habitDatabase.close()
    }

    @Test
    fun addUser_Assert_IsUserInList() = runTest {
        val habitModel = HabitModel(1, "Gaming", "description", "1", "1",true, 1, "Color")
        habitDao.insertHabit(habitModel)

        val habitList = habitDao.getAllHabitList().first()

        Truth.assertThat(habitList).contains(habitModel)
    }

    @Test
    fun addUser_And_UpdateUser_Assert_IsUpdatedUserNameRight() = runTest {
        val habitModel = HabitModel(1, "Gaming", "description", "1", "1",true, 1, "Color")
        habitDao.insertHabit(habitModel)

        val updateHabitModel = HabitModel(1, "Cricket", "description", "1", "1",true, 1, "Color")
        habitDao.insertHabit(updateHabitModel)

        val habitList = habitDao.getAllHabitList().first()

        Truth.assertThat(habitList).contains(updateHabitModel)
    }


    @Test
    fun addUser_And_DeleteUser_Assert_IsUserNotInList() = runTest {
        val habitModel = HabitModel(1, "Gaming", "description", "1", "1",true, 1, "Color")
        habitDao.insertHabit(habitModel)

        habitDao.deleteHabitModel(habitModel)

        val habitList = habitDao.getAllHabitList().first()

        Truth.assertThat(habitList).doesNotContain(habitModel)
    }

    @Test
    fun insertReset_In_HabitModel_Assert_IsInList() = runTest {
        val habitModel = HabitModel(1, "Gaming", "description", "1", "1",true, 1, "Color")
        habitDao.insertHabit(habitModel)

        val reset = ResetList(2, LocalDate.now(), LocalTime.now(), 1)
        habitDao.insertResetList(reset)

        val reset2 = ResetList(3, LocalDate.now(), LocalTime.now(), 1)
        habitDao.insertResetList(reset2)

        val newList = mutableListOf<ResetList>()
        val habitWithResetList = habitDao.getHabitWithResetList(1).collect {list ->
            Log.e("list", list.toString())
            list[0].resetList.forEach {
                Log.e("list", it.toString())
                newList.add(it)
            }
        }

        val h = habitDao.getHabitWithResetList(1).first()
        h.forEach {
            it.resetList
        }

        Truth.assertThat(newList.toList()).contains(reset)

//        Truth.assertThat(habitWithResetList).contains(HabitWithResetList(habitModel, listOf(reset, reset2)))
    }

}