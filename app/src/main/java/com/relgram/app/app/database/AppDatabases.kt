package com.relgram.app.app.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.relgram.app.app.HamsanApp

/*
  Database Handler Based On Room
 */
@Database(entities = [UserInfo::class, Settings::class, Notifications::class], version = 1)
abstract class AppDatabases : RoomDatabase() {

    companion object {
        val dataBaseName: String = "AppDatabase.db"
        var dbInstance: AppDatabases? = null
        fun getInstance(): AppDatabases? {
            if (dbInstance == null) {
                synchronized(UserInfo::class.java) {
                    if (dbInstance == null) {
                        dbInstance = Room.databaseBuilder(HamsanApp.context, AppDatabases::class.java, dataBaseName)
                                .allowMainThreadQueries()
                                .build()
                    }
                }
            }
            return dbInstance
        }
    }

    /**
     * Access to UserInfo table functions
     **/
    public abstract fun userInfoDao(): UserDao

    /**
     * Access to Settings table functions
     **/
    public abstract fun settingsDao(): SettingsDao

    /**
     * Access to Notification table functions
     **/
    public abstract fun notificationDao(): NotificationDao


    fun destroyInstance() {
        dbInstance = null
    }

}