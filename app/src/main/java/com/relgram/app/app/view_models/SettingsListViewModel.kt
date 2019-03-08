package com.relgram.app.app.view_models

import android.arch.lifecycle.ViewModel

/**
 * Create new item for show in settings activity
 *
 * @property id id of settings
 * @property title title of settings
 * @property icon icon text by iconTextView
 * @property iconColor color of icon
 */
class SettingsListViewModel(var id: Int, var title: String, var icon: String, var iconColor: Int) : ViewModel() {


}