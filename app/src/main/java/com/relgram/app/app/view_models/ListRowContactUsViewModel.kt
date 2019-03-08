package com.relgram.app.app.view_models

import com.relgram.app.app.models.ContactUsResponse
import com.relgram.app.view_models.BaseViewModel

/**
 * List ticket databinding view model
 *
 */
class ListRowContactUsViewModel : BaseViewModel() {

    var pk: Long? = null
    var title: String? = null
    var text: String? = null
    var time: String? = null
    var type: String? = null
    var answer: String? = null

    /**
     * bind to view model
     *
     * @param contact instance of ContactUsResponse from server
     */
    fun bind(contact: ContactUsResponse) {
        pk = contact.pk
        title = contact.title
        text = contact.text
        time = contact.time
        type = contact.type
        answer = contact.answer
    }

}