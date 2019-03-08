package com.relgram.app.app.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface SettingsDao {

    /**
     * Insert new settings
     * @param settings: instance of settings
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(settings: Settings)

    /**
     * delete all of settings
     */
    @Query("delete from settings")
    fun deleteAll(): Int

    /**
     * get settings by id
     * @param name: name of settings
     * @return instance of settings or null
     */
    @Query("select * from settings where name=:name")
    fun getById(name: String): Settings?

}