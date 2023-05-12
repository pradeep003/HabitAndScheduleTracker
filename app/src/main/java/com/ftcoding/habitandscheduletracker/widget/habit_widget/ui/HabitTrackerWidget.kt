package com.ftcoding.habitandscheduletracker.widget.habit_widget.ui

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.ftcoding.habitandscheduletracker.R
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.TimerModel
import com.ftcoding.habitandscheduletracker.presentation.MainActivity
import com.ftcoding.habitandscheduletracker.presentation.ui.theme.HabitAndScheduleTrackerGlanceTheme
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.hexColorToIntColor
import com.ftcoding.habitandscheduletracker.widget.SelectWidgetActivity
import com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver.HabitDataPreferences
import com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver.HabitTrackerGlanceStateDefinition
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class HabitTrackerWidget : GlanceAppWidget(errorUiLayout = R.layout.app_widget_custom_error) {

    override val stateDefinition: GlanceStateDefinition<HabitDataPreferences>
        get() = HabitTrackerGlanceStateDefinition

    @Composable
    override fun Content() {

        // get the preference state
        val glanceState = currentState<HabitDataPreferences>()
        // get first item in list
        val list = glanceState.prefData

        HabitAndScheduleTrackerGlanceTheme {

//             if preference list is empty display empty widget content
            if (list.isEmpty()) {
                EmptyWidgetContent()
            } else {
                list.forEach {
                    // if current appwidget glance id is same of preference datastore event glance id
                    if (it.glanceId == LocalGlanceId.current.hashCode()) {
                        it.list?.let { model ->
                            HabitTrackerWidgetContent(habitModel = model)
                        }
                    } else {
                        EmptyWidgetContent()
                    }
                }
            }
        }

    }

}

// empty widget content
@Composable
fun EmptyWidgetContent() {
    Row(
        modifier = GlanceModifier
            .cornerRadiusCompat(6, MaterialTheme.colorScheme.background.hashCode(), 1f)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(actionStartActivity<SelectWidgetActivity>()),
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {

        Image(
            provider = ImageProvider(R.drawable.app_icon),
            contentDescription = "app_icon",
            modifier = GlanceModifier.size(40.dp).clickable(actionStartActivity<SelectWidgetActivity>())
        )

        Spacer(modifier = GlanceModifier.width(4.dp))

        Text(
            text = LocalContext.current.getString(R.string.tap_to_select),
            style = TextStyle(color = ColorProvider(MaterialTheme.colorScheme.onBackground)),
            modifier = GlanceModifier.clickable(actionStartActivity<SelectWidgetActivity>())
        )

    }
}


@Composable
fun HabitTrackerWidgetContent(
    habitModel: HabitModel
) {

    val trackerTimer by remember {
        mutableStateOf(calculateTimeDifference(habitModel.habitLastResetTime))
    }

    Row(
        modifier = GlanceModifier
            .clickable(actionStartActivity<MainActivity>())
            .cornerRadiusCompat(6, habitModel.habitColor.hexColorToIntColor.hashCode(), 1f)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {

        Image(
            provider = ImageProvider(habitModel.habitIcon),
            contentDescription = "habit_icon",
            modifier = GlanceModifier.size(40.dp).padding(4.dp).clickable(actionStartActivity<MainActivity>())
        )

        Spacer(modifier = GlanceModifier.width(4.dp))

        Column(
            modifier = GlanceModifier.padding(4.dp),
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {

            Text(
                text = habitModel.habitTitle,
                style = TextStyle(color = ColorProvider(Color.White), fontWeight = FontWeight.Bold, fontSize = 18.sp),
                modifier = GlanceModifier.clickable(actionStartActivity<MainActivity>()),
                maxLines = 1
            )


            Row(
                modifier = GlanceModifier,
                verticalAlignment = Alignment.Vertical.Bottom,
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {


                Text(
                    text = trackerTimer.days.toString(),
                    style = TextStyle(ColorProvider(Color.White), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )

                Text(
                    text = " day  ",
                    style = TextStyle(ColorProvider(Color.White), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
                Text(
                    text = trackerTimer.hours.toString(),
                    style = TextStyle(ColorProvider(Color.White), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )

                Text(
                    text = " hours  ",
                    style = TextStyle(ColorProvider(Color.White), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )

                Text(
                    text = trackerTimer.minutes.toString(),
                    style = TextStyle(ColorProvider(Color.White), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )

                Text(
                    text = " minute  ",
                    style = TextStyle(
                        ColorProvider(Color.White),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        }

    }
}


// tracker details
fun calculateTimeDifference(startTime: String): TimerModel {
    val obj = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    var timerModel = TimerModel(0, 0, 0, 0)

    val calendar = Calendar.getInstance()
    val selectedTime = obj.parse(startTime)
    val currentTime = obj.parse(
        "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
            calendar.get(
                Calendar.YEAR
            )
        } ${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
    )
    val timeDifference = selectedTime?.time?.let {
        currentTime?.time?.minus(it)
    }

    if (timeDifference != null) {
        // Calculate time difference in days using TimeUnit class
        val daysDifference: Long = TimeUnit.MILLISECONDS.toDays(timeDifference) % 365
        // Calculate time difference in years using TimeUnit class
        TimeUnit.MILLISECONDS.toDays(timeDifference) / 365L
        // Calculate time difference in seconds using TimeUnit class
        val secondsDifference: Long = TimeUnit.MILLISECONDS.toSeconds(timeDifference) % 60
        // Calculate time difference in minutes using TimeUnit class
        val minutesDifference: Long = TimeUnit.MILLISECONDS.toMinutes(timeDifference) % 60
        // Calculate time difference in hours using TimeUnit class
        val hoursDifference: Long = TimeUnit.MILLISECONDS.toHours(timeDifference) % 24
        // Show difference in years, in days, hours, minutes, and s

        timerModel =
            TimerModel(daysDifference, hoursDifference, minutesDifference, secondsDifference)
    }


    return timerModel
}

// make round corner below android 13
fun GlanceModifier.cornerRadiusCompat(
    cornerRadius: Int,
    @ColorInt color: Int,
    @FloatRange(from = 0.0, to = 1.0) backgroundAlpha: Float = 1f
): GlanceModifier {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        this.background(Color(color = color).copy(alpha = backgroundAlpha))
            .cornerRadius(cornerRadius.dp)
    } else {
        val radii = FloatArray(8) { cornerRadius.toFloat() }
        val shape = ShapeDrawable(RoundRectShape(radii, null, null))
        shape.paint.color = ColorUtils.setAlphaComponent(color, (255 * backgroundAlpha).toInt())
        val bitmap = shape.toBitmap(width = 150, height = 75)
        this.background(BitmapImageProvider(bitmap))
    }
}