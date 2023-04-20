package com.ftcoding.habitandscheduletracker

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ftcoding.habitandscheduletracker.notification.receiver.AppWorkerClass
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.schedule.ScheduleUseCases
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class HabitAndScheduleTrackerApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()


    override fun onCreate() {
        super.onCreate()
        setUpRecurringWork()
    }

    private fun delayInit() {
        applicationScope.launch {

            setUpRecurringWork()
        }
    }

    private fun setUpRecurringWork() {
        val repeatingRequest = PeriodicWorkRequestBuilder<AppWorkerClass>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            AppWorkerClass.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

}