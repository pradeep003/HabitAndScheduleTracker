package com.ftcoding.habitandscheduletracker.data.dao

import androidx.room.*
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.relation.HabitWithResetList
import kotlinx.coroutines.flow.Flow


@Dao
interface HabitDao {

    @Query("SELECT * FROM habit")
    fun getAllHabitList() : Flow<List<HabitModel>>

    @Query("SELECT * FROM habit WHERE habitId = :id")
    suspend fun getHabitById(id: Int): HabitModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habitModel: HabitModel): Long

    @Delete
    suspend fun deleteHabitModel(habitModel: HabitModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResetList(resetTime: ResetList)

    @Transaction
    @Query("SELECT * FROM habit WHERE habitId = :habitId")
    fun getHabitWithResetList(habitId: Int): Flow<List<HabitWithResetList>>

}