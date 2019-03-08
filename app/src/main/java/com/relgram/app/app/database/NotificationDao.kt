package com.relgram.app.app.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface NotificationDao {
    /**
     * Inset new Notification to table
     *  @param notifications: Instance Of Notification
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notifications: Notifications)

    /**
     * Delete all notification
     * @return Successful Action
     */
    @Query("delete from notifications")
    fun deleteAll(): Int

    /**
     * Get notification by Id
     * @param id: primary key of table
     * @return instance of notification or null
     */
    @Query("select * from notifications where id=:id")
    fun getById(id: Int): Notifications?

    /**
     * Get list of all notification
     * @return List of all notifications or null
     */
    @Query("select * from notifications")
    fun getAll(): List<Notifications>?

}