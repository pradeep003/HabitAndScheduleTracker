package com.ftcoding.habitandscheduletracker.notification

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Rect
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.*
import android.util.Log
import android.view.GestureDetector
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.*
import com.ftcoding.habitandscheduletracker.notification.activity_utils.turnScreenOffAndKeyguardOn
import com.ftcoding.habitandscheduletracker.notification.activity_utils.turnScreenOnAndKeyguardOff
import com.ftcoding.habitandscheduletracker.presentation.ui.theme.HabitAndScheduleTrackerTheme
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_DESC
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_TITLE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AlarmLockScreenActivity : ComponentActivity() {


    private lateinit var vibrator: Vibrator
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var ringtone: Ringtone


    @SuppressLint("ServiceCast", "SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        turnScreenOnAndKeyguardOff()

        val intent = intent.extras
        val eventTitle = intent?.getString(NOTIFICATION_TITLE, "")
        val eventDesc = intent?.getString(NOTIFICATION_DESC, "")

        // initialize vibrator
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }


        // start vibration
        // vibration pattern
        val pattern = longArrayOf(0, 800, 200, 1200, 300, 2000, 400, 4000, 500, 5000)
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))

        // play sound with vibration
        val notifyMusic = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, notifyMusic)
        ringtone.play()

        setContent {

            val context = LocalContext.current

            val activity = context as Activity
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            HabitAndScheduleTrackerTheme() {
                LockScreenContent(eventTitle ?: "", eventDesc ?: "") {
                    this.finish()
                }
            }
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        // Add a listener to update the behavior of the toggle fullscreen button when
        // the system bars are hidden or revealed.
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            // You can hide the caption bar even when the other system bars are visible.
            // To account for this, explicitly check the visibility of navigationBars()
            // and statusBars() rather than checking the visibility of systemBars().

            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val left = Rect(200, 400, 200, 200)
                val right = Rect(200, 400, 200, 200)
                view.systemGestureExclusionRects = listOf(left, right)
            }

            view.onApplyWindowInsets(windowInsets)
        }



        // Tell the window that we want to handle/fit any system windows
        WindowCompat.setDecorFitsSystemWindows(window, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
        vibrator.cancel()
        ringtone.stop()
    }

}

@Composable
fun WavesAnimation(modifier: Modifier) {

    // list of wave
    val waves = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )

    // animation for wave
    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(4000, easing = FastOutLinearInEasing),
        repeatMode = RepeatMode.Restart
    )

    // apply animation for each wave one by one
    waves.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 1000L)
            animatable.animateTo(
                targetValue = 1f, animationSpec = animationSpec
            )
        }
    }

    val dys = waves.map { it.value }

    // wave ui
    Box(modifier = modifier
        .fillMaxWidth()
    ) {
        dys.forEach { dy ->
            Box(modifier = Modifier
                .size(60.dp)
                .background(Color.White)
                .align(Alignment.Center)
                .graphicsLayer {
                    scaleX = dy * 4 + 1
                    scaleY = dy * 4 + 1
                    alpha = 1 - dy
                }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White, shape = CircleShape)
                )
            }
        }

        // alarm on icon
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .background(color = Color.White, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.AlarmOn,
                contentDescription = "alarm on icon",
                tint = Color.Black,
                modifier = Modifier
                    .size(50.dp)
                    .align(
                        Alignment.Center
                    )

            )
        }
    }
}

// swipe up to close activity animation
@Composable
private fun SwipeUpAnimation(modifier: Modifier) {

    val swipeUp = remember {
        Animatable(0f)
    }

    val offsetAnimation: Dp by animateDpAsState(
        targetValue =
        if (swipeUp.value == 0f) 0.dp else 6.dp
    )

    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(
            2000, easing = LinearOutSlowInEasing
        ),
        repeatMode = RepeatMode.Restart
    )

    LaunchedEffect(swipeUp) {
        swipeUp.animateTo(
            targetValue = 1f, animationSpec = animationSpec
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp), contentAlignment = Alignment.BottomCenter
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = swipeUp.value
                    scaleY = swipeUp.value
                    alpha = 1 - swipeUp.value
                }
                .absoluteOffset(y = -offsetAnimation)
        ) {

            Icon(
                imageVector = Icons.Filled.KeyboardDoubleArrowUp,
                contentDescription = "swipe up icon",
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun LockScreenContent(
    eventTitle: String,
    eventDesc: String,
    activityFinishListener: () -> Unit
) {

    val scope = rememberCoroutineScope()

    // store the value for swipe up gesture
    var offsetY by remember {
        mutableStateOf(0f)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                // render drag gestures
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val y = dragAmount.y

                    when {
                        y < 0 -> {
                            scope.launch {
                                // when swipe up close the activity
                                // activity finish listener triggered
                                activityFinishListener()

                            }
                        }
                    }
                    offsetY += dragAmount.y
                }
            },

        color = Color.Black,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(4.dp))

            // date and time ui
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Text(
                    text = DateTimeFormatter.ofPattern("hh:mm a")
                        .format(LocalTime.now()),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Text(
                    text = DateTimeFormatter.ofPattern("dd MMM yyyy")
                        .format(LocalDate.now()),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }

            // event title and event desc ui
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = eventTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = eventDesc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

            }

            Spacer(modifier = Modifier.height(4.dp))

            WavesAnimation(modifier = Modifier)

            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SwipeUpAnimation(modifier = Modifier)

                Button(onClick = {
                    // when clicked trigger activityFinishListener() which will close this activity
                    activityFinishListener()
                }) {
                    Text(
                        text = "Swipe up to turn off alarm",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }



                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun WaveAnimationPreview() {
//
//    AlarmLockScreenActivity()
//}