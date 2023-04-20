package com.ftcoding.habitandscheduletracker.di

import android.content.Context
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//@Module
//@InstallIn(SingletonComponent::class)
//object WorkManagerInitializer : Initializer<WorkManager> {
//    override fun create(context: Context): WorkManager {
//        val configuration = Configuration.Builder().build()
//        WorkManager.initialize(context, configuration)
//        return WorkManager.getInstance(context)
//    }
//
//    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
//        // No dependencies on other libraries.
//        return emptyList<Class<out Initializer<*>>>().toMutableList()
//    }
//}