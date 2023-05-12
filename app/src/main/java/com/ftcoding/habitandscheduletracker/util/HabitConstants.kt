package com.ftcoding.habitandscheduletracker.util

import com.ftcoding.habitandscheduletracker.R
import com.ftcoding.habitandscheduletracker.data.domain.models.data.HabitIcon
import java.util.concurrent.TimeUnit

object HabitConstants {

    const val USER_DATA = "user_data"
    const val HABIT_TABLE_NAME = "habit"
    const val HABIT_RESET_TABLE_NAME = "habit_reset"
    const val HABIT_USER_DATA_TABLE_NAME = "habit_data"
    const val CUSTOM_DAYS_TABLE_NAME = "habit_custom_days"
    const val CUSTOM_HOURS_TABLE_NAME = "habit_custom_hours"
    const val CUSTOM_WEEKS_TABLE_NAME = "habit_custom_weeks"
    const val KEY_TITLE : String = "habit_title"
    const val KEY_DESC = "habit_description"
    const val KEY_START_TIME = "habit_start_time"
    const val KEY_LAST_RESET_TIME = "key_last_reset_time"
    const val KEY_FREQUENCY = "habit_frequency"
    const val KEY_NOTIFY = "habit_notify"
    const val KEY_ICON = "habit_icon"
    const val KEY_COLOR = "habit_color"
    const val RESET_LIST = "habit_reset_list"
    const val KEY_HABIT_TARGET = "habit_target"
    const val KEY_HABIT_ACHIEVEMENT = "habit_achievement"
    const val KEY_CUSTOM_DAYS_LIST = "custom_days_of_week_list"
    const val KEY_CUSTOM_NUM_OF_DAYS = "custom_num_of_days"
    const val KEY_CUSTOM_DAYS_TIME = "custom_days_time"
    const val KEY_CUSTOM_HOUR_REPEAT_TIME = "custom_hour_repeat_time"
    const val KEY_CUSTOM_HOURS_LIST_TIME = "custom_hours_list_time"
    const val KEY_CUSTOM_WEEK_REPEAT = "custom_week_repeat"
    const val KEY_CUSTOM_WEEK_REPEAT_DAY = "custom_week_repeat_day"
    const val KEY_CUSTOM_WEEK_REPEAT_TIME = "custom_week_repeat_time"
    const val NONE = "None"
    const val CUSTOM_HOURS = "Custom hours"
    const val CUSTOM_DAY = "Every Day"
    const val CUSTOM_WEEK = "Every Week"

    const val NOTIFICATION_ID = "event_id"
    const val NOTIFICATION_IS_ALARM = "event_is_alrm"
    const val NOTIFICATION_TITLE = "event_title"
    const val NOTIFICATION_DESC = "event_details"
    const val NOTIFICATION_START_HOUR = "event_start_hour"
    const val NOTIFICATION_START_MINUTE = "event_start_minute"
    const val NOTIFICATION_ICON = "event_icon"
    const val NOTIFICATION_REPEAT_LIST = "event_repeat_list"

    const val SCHEDULE_TABLE_NAME = "Schedule"
    const val SCHEDULE_TRACKER_TABLE_NAME = "schedule_time_tracker"
    const val SCHEDULE_NOTES_TABLE_NAME = "schedule_notes"

    const val FOREGROUND_NOTIFICATION_ID = 1
    const val USER_ID = 1


    val HABIT_ACHIEVEMENT = arrayOf(TimeUnit.DAYS.toMillis(1), TimeUnit.DAYS.toMillis(3), TimeUnit.DAYS.toMillis(7), TimeUnit.DAYS.toMillis(14), TimeUnit.DAYS.toMillis(28), TimeUnit.DAYS.toMillis(84), TimeUnit.DAYS.toMillis(168), TimeUnit.DAYS.toMillis(336))

    val FREQUENCY_LIST = arrayOf(NONE, CUSTOM_HOURS, CUSTOM_DAY, CUSTOM_WEEK)

    val NOTIFY_TYPE = arrayOf("Notification", "Alarm")

    val HABIT_ICON_LIST = arrayListOf<HabitIcon>(
        HabitIcon("chocolate", R.drawable.chocolate),
        HabitIcon("razor", R.drawable.razor),
        HabitIcon("alarm", R.drawable.alarm_clock),
        HabitIcon("alcohol", R.drawable.alcohol),
        HabitIcon("bicycle", R.drawable.bicycle),
        HabitIcon("book", R.drawable.book),
        HabitIcon("caffeine", R.drawable.caffeine),
        HabitIcon("cigarette", R.drawable.cigarette),
        HabitIcon("cocaine", R.drawable.cocaine),
        HabitIcon("dish_washing", R.drawable.dish_washing),
        HabitIcon("pen", R.drawable.fountain_pen),
        HabitIcon("gambling", R.drawable.gambling),
        HabitIcon("game", R.drawable.game),
        HabitIcon("garbage", R.drawable.garbage),
        HabitIcon("gym", R.drawable.gym),
        HabitIcon("heroin", R.drawable.heroin),
        HabitIcon("food", R.drawable.junk_food),
        HabitIcon("kleptomania", R.drawable.kleptomania),
        HabitIcon("laundry_basket", R.drawable.laundry_basket),
        HabitIcon("love", R.drawable.love),
        HabitIcon("lsd", R.drawable.lsd),
        HabitIcon("marijuana", R.drawable.marijuana),
        HabitIcon("money", R.drawable.money),
        HabitIcon("mop", R.drawable.mop),
        HabitIcon("mortarboard", R.drawable.mortarboard),
        HabitIcon("movie", R.drawable.movie),
        HabitIcon("no_alcohol", R.drawable.no_alchol),
        HabitIcon("no_entry", R.drawable.no_entry),
        HabitIcon("pipe", R.drawable.pipe),
        HabitIcon("poison", R.drawable.poison),
        HabitIcon("self_harm", R.drawable.self_harm),
        HabitIcon("shower", R.drawable.shower),
        HabitIcon("slot", R.drawable.slot_machine),
        HabitIcon("smartphone", R.drawable.smartphone),
        HabitIcon("snack", R.drawable.snack),
        HabitIcon("social_media", R.drawable.social_media),
        HabitIcon("soda", R.drawable.soda),
        HabitIcon("sweet_food", R.drawable.sweet_food),
        HabitIcon("tea", R.drawable.tea),
        HabitIcon("tv", R.drawable.tv),
        HabitIcon("wine", R.drawable.wine)


    )
}