package com.ftcoding.habitandscheduletracker.presentation.setting_ui

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftcoding.habitandscheduletracker.data.domain.models.data.MusicModel
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.schedule.ScheduleUseCases
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user.UserUseCases
import com.ftcoding.habitandscheduletracker.util.HabitConstants.USER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val scheduleUseCases: ScheduleUseCases
) : ViewModel() {

    init {
        refreshData()
    }

    private val _user = mutableStateOf(User())
    val user: State<User> = _user


    fun saveNewUserName(username: String) {
        // change username in user state
        _user.value = user.value.copy(userName = username)

        // than save username
        viewModelScope.launch {
            userUseCases.insertUserUseCase.invoke(
                user.value
            )
        }
    }

    private fun refreshData () {
        viewModelScope.launch {
            // fetch user details
            userUseCases.getUserUseCase.invoke().collect { list ->
                if (list.isNotEmpty()) {
                    list.first {
                        if (it.userId == USER_ID) {
                            _user.value = it
                            return@first true
                        } else {
                            _user.value = User()
                            return@first false
                        }
                    }
                }
            }


        }
    }

    fun saveNewUserProfileImage(uri: Uri) {
        // change username in user state
        _user.value = user.value.copy(image = uri.toString())
        // than save username
        viewModelScope.launch {
            userUseCases.insertUserUseCase.invoke(
                user.value
            )
        }
    }

    fun getUserProfileImage() : String? {
        return user.value.image
    }

    val musicModelList = mutableStateListOf<MusicModel>()

    private val _ringtone = mutableStateOf(MusicModel(contentUri = Uri.EMPTY, -1, ""))
    val ringtone: State<MusicModel> = _ringtone

    // set the new ringtone in viewModel state
    fun setNewRingtone(ringtone: MusicModel) {
        _ringtone.value = ringtone
    }

    // save the new ringtone in ViewModel
    fun saveNewRingtone(ringtonePath: String) {
        _user.value = user.value.copy(ringtonePath = ringtonePath)
        viewModelScope.launch {
            userUseCases.insertUserUseCase.invoke(user.value)
        }
    }

    // theme primary color for app

    fun setPrimaryColor(color: String) {
        _user.value = user.value.copy(themeColor = color)
        viewModelScope.launch {
            userUseCases.insertUserUseCase.invoke(user.value)
        }
    }

    // dark mode state

    fun setDarkMode(isDarkMode: Boolean) {
        _user.value = user.value.copy(darkMode = isDarkMode)
        viewModelScope.launch {
            userUseCases.insertUserUseCase.invoke(user.value)
        }
    }

    private val _list = mutableStateOf<List<Event>>(emptyList())
    val list: State<List<Event>> = _list

     fun getAllScheduleEvent(): List<Event>? {
        var eventList : List<Event>? = null
        viewModelScope.launch {
            scheduleUseCases.getAllScheduleEvents.invoke().collect { list ->
                if (list.isNotEmpty()) {
                    _list.value = list
                    eventList = list
                }
            }
        }
        return eventList
    }


    private var initialized = false
    private val backgroundScope = viewModelScope.plus(Dispatchers.Default)

    fun initializeListIfNeeded(context: Context) {
        if (initialized) return
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME

        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
        val query = context.contentResolver.query(
            collection, projection, selection, null, sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
                )
                musicModelList.add(
                    MusicModel(
                        contentUri = contentUri,
                        songId = id,
                        songTitle = title
                    )
                )

            }

        }
        initialized = true
    }
}

