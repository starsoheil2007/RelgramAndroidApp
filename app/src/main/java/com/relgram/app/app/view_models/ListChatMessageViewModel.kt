package com.relgram.app.app.view_models

import android.view.View
import com.relgram.app.app.models.ChatTextItem
import com.relgram.app.app.webservice.WebService
import com.relgram.app.view_models.BaseViewModel

/**
 * Chat message view model
 *
 */
class ListChatMessageViewModel : BaseViewModel() {
    var text: String? = null
    var time: String? = null
    var image: String? = null
    var userAvatar: String? = null
    var userFirstName: String? = null

    /**
     * binf data to view
     *
     * @param chat instance of ChatTextItem
     */
    fun bind(chat: ChatTextItem) {
        text = chat.text
        time = chat.time
        image = chat.image
        userAvatar = chat.userAvatar
        userFirstName = chat.userFirstName
    }

    /**
     * get full user avatar
     *
     * @return full string url
     */
    fun getFullAvatarUrl(): String {
        return WebService.PICTURE_URL + userAvatar
    }

    /**
     * format chat time (time from server has html tag)
     * this function remove html tags
     *
     * @return formated Time
     */
    fun getChatTimeFormated(): String {
        return this!!.time!!.replace("<b>", "")
                .replace("</b>", "")
    }

    /**
     * get full image if image chat
     *
     * @return full string url
     */
    fun getFullImage(): String {
        return WebService.PICTURE_URL + image
    }

    /**
     * Check is text chat for show or hide textView in view
     *
     * @return view Visibility
     */
    fun isTextMessage(): Int {
        if (text != null) {
            return View.VISIBLE
        }
        return View.GONE
    }

    /**
     * Check is image chat for show or hide imageView in view
     *
     * @return view Visibility
     */
    fun isImageMessage(): Int {
        if (image != null) {
            return View.VISIBLE
        }
        return View.GONE
    }


}