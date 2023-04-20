package com.ftcoding.habitandscheduletracker.data.db

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ftcoding.habitandscheduletracker.R
import com.ftcoding.habitandscheduletracker.data.dao.UserDao
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.util.HabitConstants.USER_ID
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDatabaseTest {

    private lateinit var userDatabase: UserDatabase
    private lateinit var userDao: UserDao
    private lateinit var bitmap: Bitmap

    @Before
    fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        userDatabase = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
        userDao = userDatabase.userDao

        val drawable = ContextCompat.getDrawable(context, R.drawable.trophy)
        bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

    }

    @After
    fun tearDown() {
        userDatabase.close()
    }


    @Test
    fun addUser_Assert_IsUsernameSame() = runTest {
        val user = User(USER_ID, "Pradeep", bitmap)
        userDao.insertUser(user)

        val getUser = userDao.getUser().collect{
            it.first().userName
        }
        Truth.assertThat(user.userName).isEqualTo(getUser)
    }


}