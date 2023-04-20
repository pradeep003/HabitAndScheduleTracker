package com.ftcoding.habitandscheduletracker.data.domain.models.user

import android.graphics.Bitmap
import androidx.compose.material3.MaterialTheme
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ftcoding.habitandscheduletracker.util.HabitConstants
import com.ftcoding.habitandscheduletracker.util.HabitConstants.USER_ID

@Entity
data class User (

    @PrimaryKey
    val userId: Int = USER_ID,

    @ColumnInfo
    val userName: String = "Buddy",

    @ColumnInfo
    val image: String? = null,

    @ColumnInfo
    val ringtonePath: String = "",

    @ColumnInfo
    val themeColor: String = "#FF006A6A",

    @ColumnInfo
    val darkMode: Boolean = false

        )