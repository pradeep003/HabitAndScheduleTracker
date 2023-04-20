package com.ftcoding.habitandscheduletracker.data.domain.models.data

import android.net.Uri

data class MusicModel(
    val contentUri: Uri,
    val songId: Long,
    val songTitle: String
)