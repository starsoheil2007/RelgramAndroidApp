package com.relgram.app.app.view_models

import com.relgram.app.app.models.ChatHistoryResponse
import com.relgram.app.app.webservice.WebService
import com.relgram.app.view_models.BaseViewModel

/**
 * List Of Chat History Data Binding Model
 *
 */
class ListChatHistoryViewModel : BaseViewModel() {

    var chatId: Long? = null
    var count: Long? = null
    var isBlocked: Boolean? = null
    var chatTime: String? = null
    var blockedBy: String? = null
    var userId: Long? = null
    var userFirstName: String? = null
    var userAvatar: String? = null
    /**
     * function to bind data
     *
     * @param chat instance of ChatHistoryResponse
     */
    fun bind(chat: ChatHistoryResponse) {
        chatId = chat.chatId
        count = chat.count
        isBlocked = chat.isBlocked
        chatTime = chat.chatTime
        blockedBy = chat.blockedBy
        userId = chat.userId
        userFirstName = chat.userFirstName
        userAvatar = chat.userAvatar
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
        return this.chatTime!!.replace("<b>", "")
                .replace("</b>", "")
    }

}