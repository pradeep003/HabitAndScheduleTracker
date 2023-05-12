package com.ftcoding.habitandscheduletracker.presentation.data_source.repository

import com.ftcoding.habitandscheduletracker.data.dao.HabitDao
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.repository.HabitRepository
import com.ftcoding.habitandscheduletracker.rule.CoroutineDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class HabitRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val coroutineDispatcherRule = CoroutineDispatcherRule()

    @RelaxedMockK lateinit var habitDao: HabitDao
    lateinit var habitRepository: HabitRepository

    @Before
    fun setUp() {
        habitRepository = HabitRepositoryImpl(habitDao)
    }

    @After
    fun tearDown() {
    }


//    @Test
//    fun addHabitModel_And_GetHabit_Verify_InsertUser_And_GetAllHabitModelWereCalled() = runTest {
//        val habitModel = HabitModel(1, "Gaming", "description", "1", true, 1, "Color")
//        coEvery {
//            habitDao.insertHabit(any())
//        } just Runs
//        habitRepository.insertNewHabit(habitModel)
//
//        coEvery {
//            habitDao.getAllHabitList()
//        } returns callbackFlow {
//            trySend(listOf(habitModel))
//        }
//        habitRepository.getAllHabitData().first()
//
//        coVerifyAll {
//            habitDao.insertHabit(any())
//            habitDao.getAllHabitList()
//        }
//    }
}