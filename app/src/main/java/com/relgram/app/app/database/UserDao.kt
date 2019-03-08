package com.relgram.app.app.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface UserDao {

    /**
     * Insert new User Info
     * @param userInfo: instance of UserInfo
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userInfo: UserInfo)

    /**
     * Delete all User Info
     */
    @Query("delete from user_info")
    fun deleteAll(): Int

    /**
     * Get token of user for rest api header
     * @return String token or empty
     */
    @Query("select token from user_info LIMIT 1")
    fun getToken(): String

    /**
     * Get User info
     * @return instance of UserInfo
     */
    @Query("select * from user_info LIMIT 1")
    fun get(): UserInfo


}