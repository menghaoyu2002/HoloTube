package com.holotube.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChannelEntity::class], version = 1, exportSchema = false)
abstract class FollowingDatabase : RoomDatabase() {
    abstract val channelDao: ChannelDao

    companion object {
        @Volatile
        private var INSTANCE: FollowingDatabase? = null

        fun getInstance(context: Context): FollowingDatabase {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    FollowingDatabase::class.java,
                    "following_database"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }
            return instance
        }
    }
}