package com.relgram.app.app.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Notifications table
 * @param id:primary key
 * @param title: title of message
 * @param text:body of message
 */
@Entity(tableName = "notifications")
class Notifications(@PrimaryKey(autoGenerate = true) var id: Int? = null, var title: String? = null, var text: String? = null) {}