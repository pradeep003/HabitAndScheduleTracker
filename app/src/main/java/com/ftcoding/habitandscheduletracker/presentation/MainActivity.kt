package com.ftcoding.habitandscheduletracker.presentation

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.notification.receiver.AppWorkerClass
import com.ftcoding.habitandscheduletracker.notification.receiver.ScheduleNotificationReceiver
import com.ftcoding.habitandscheduletracker.notification.scheduleAppNotificationDaily
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user.UserUseCases
import com.ftcoding.habitandscheduletracker.presentation.ui.navigation.BottomBar
import com.ftcoding.habitandscheduletracker.presentation.ui.navigation.Navigation
import com.ftcoding.habitandscheduletracker.presentation.ui.theme.HabitAndScheduleTrackerTheme
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.hexColorToIntColor
import com.ftcoding.habitandscheduletracker.util.HabitConstants.USER_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    lateinit var receiver: ScheduleNotificationReceiver

    @Inject
    lateinit var userUseCases: UserUseCases
//    lateinit var darkMode : MutableState<Boolean>

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receiver = ComponentName(applicationContext, ScheduleNotificationReceiver::class.java)
        applicationContext.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )


//        var darkMode : MutableState<Boolean> = mutableStateOf(false)
//        lifecycleScope.launch {
//            darkMode = mutableStateOf(userUseCases.getUserUseCase.invoke(USER_ID).darkMode)
//
//        }
//        this.applicationContext.scheduleAppNotificationDaily()

        // Created a Work Request
//        val uploadWorkRequest: WorkRequest =
//            PeriodicWorkRequestBuilder<AppWorkerClass>(1, TimeUnit.DAYS).build()
//        // Submit the WorkRequest to the system\
//        WorkManager.getInstance(applicationContext).enqueue(uploadWorkRequest)


//        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
//        receiver = ScheduleNotificationReceiver()

//        IntentFilter(Intent.ACTION_RE/BOOT).also {
//            Toast.makeText(applicationContext, "intent filter", Toast.LENGTH_LONG).show()
//        }


//        val jobInfo = JobInfo.Builder(1, ComponentName(this, JobS::class.java))
//        val job = jobInfo.setPersisted(true).build()
//        jobScheduler.schedule(job)

        setContent {

            // state to observe setting changes
            val settingDetails = remember {
                mutableStateOf(User())
            }

            // observe the changes made in setting
            if (userUseCases.getUserUseCase.invoke()
                    .collectAsState(initial = listOf(User())).value.isNotEmpty()
            ) {
                userUseCases.getUserUseCase.invoke()
                    .collectAsState(initial = listOf(User())).value.first {
                        if (it.userId == USER_ID) {
                            settingDetails.value = it
                            return@first true
                        } else {
                            settingDetails.value = User()
                            return@first false
                        }
                    }
            }

            HabitAndScheduleTrackerTheme(
                primaryColor = settingDetails.value.themeColor.hexColorToIntColor,
                darkTheme = settingDetails.value.darkMode
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            Navigation(navController = navController, padding = it)
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(receiver)
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    HabitAndScheduleTrackerTheme {
//        Greeting("Android")
//    }
//}

//class JobS : JobService() {
//    override fun onStartJob(params: JobParameters?): Boolean {
//        applicationContext.showNotificationWithFullScreenIntent(eventName = "service", eventDesc = "desc", eventIcon = com.ftcoding.habitandscheduletracker.R.drawable.chocolate)
//        Toast.makeText(applicationContext, "start job", Toast.LENGTH_LONG).show()
//        return true
//    }
//
//    override fun onStopJob(params: JobParameters?): Boolean {
//        Toast.makeText(applicationContext, "stop job", Toast.LENGTH_LONG).show()
//        return true
//    }
//
//}