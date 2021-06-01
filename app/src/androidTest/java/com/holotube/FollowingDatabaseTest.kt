package com.holotube

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.holotube.database.ChannelDao
import com.holotube.database.ChannelEntity
import com.holotube.database.FollowingDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FollowingDatabaseTest {

    private lateinit var channelDao: ChannelDao
    private lateinit var db: FollowingDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, FollowingDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        channelDao = db.channelDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun followChannel() {
        val channel = ChannelEntity(
            "lNVQfuc6D4Y",
            "[Super Mario 3D World + Bowsers Fury] shaaaaaaak",
            "2021-06-01T00:00:00.000Z",
            null,
            null,
            "lNVQfuc6D4Y",
            "https://yt3.ggpht.com/ytc/AAUvwnhSSaF3Q-PyyTSis4EH6Cu8FZ32LNvkxI9Gl_rn=s800-c-k-c0x00ffffff-no-rj",
            "https://img.youtube.com/vi/lNVQfuc6D4Y/maxresdefault.jpg",
            "https://www.youtube.com/live_chat?is_popout=1&v=lNVQfuc6D4Y",
        )
        channelDao.follow(channel)
        val following = channelDao.get("Gawr Gura Ch. hololive-EN")
        assertEquals(following?.channelName, "Gawr Gura Ch. hololive-EN")
    }
}