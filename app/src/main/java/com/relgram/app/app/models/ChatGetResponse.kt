package com.relgram.app.app.models

/**
 * Get Chat Response (user/chat) Rest
 *
 * @property chatId id of chat
 * @property count count of chat in this conversion
 * @property isBlocked is blocked chat and can send message check
 * @property chatTime time of last message
 * @property blockedBy witch user blocked chat
 * @property text list of text message in this conversion
 */
data class ChatGetResponse(var chatId: Long, var count: Long, var isBlocked: Boolean, var chatTime: String, var blockedBy: String, var text: List<ChatTextItem>)

/**
 * Get One Chat conversion Response
 *
 * only one of text or message can be filled
 * @property userId sender user id
 * @property userFirstName sender first name
 * @property text text of message
 * @property time time of message
 * @property image image if image message
 * @property userAvatar sender avatar
 * @property isSelf if sender is this user is true
 */
data class ChatTextItem(var userId: Long, var userFirstName: String, var text: String, var time: String, var image: String, var userAvatar: String, var isSelf: Boolean)