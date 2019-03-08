package com.relgram.app.app.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Settings table
 * this settings will be received from server
 * @param id:primary key
 * @param name:name of settings (number)
 * @param value:values of settings
 */
@Entity(tableName = "settings")
class Settings(@PrimaryKey(autoGenerate = true) var id: Int? = null, var name: String? = null, var value: String? = null) {}