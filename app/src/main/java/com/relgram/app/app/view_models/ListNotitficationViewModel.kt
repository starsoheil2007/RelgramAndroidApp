package com.relgram.app.app.view_models

import com.relgram.app.app.database.Notifications
import com.relgram.app.view_models.BaseViewModel

/**
 * notification activity view model
 *
 */
class ListNotitficationViewModel : BaseViewModel() {

    var pk: Long? = null
    var title: String? = null
    var text: String? = null

    /**
     * bind notification to view model
     *
     * @param contact instance of Notifications
     */
    fun bind(contact: Notifications) {
        title = contact.title
        text = contact.text
    }

}