package com.holotube.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.holotube.network.Channel

@Database(entities = [ChannelEntity::class], version = 1, exportSchema = false)
abstract class FollowingDatabase : RoomDatabase() {
    abstract val channelDao: ChannelDao

    companion object {
        @Volatile
        private var INSTANCE: FollowingDatabase? = null

        fun getInstance(context: Context): FollowingDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FollowingDatabase::class.java,
                        "following_database"
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }
    }
}