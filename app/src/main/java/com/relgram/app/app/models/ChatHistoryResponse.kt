package com.relgram.app.app.models

/**
 * Get Chat history response (user/chat_history)
 *
 * @property chatId id of chat in server
 * @property count count of message in this conversion
 * @property isBlocked is blocked by one user
 * @property chatTime time of last message
 * @property blockedBy witch user blocked this chat
 * @property userId last massage sender id
 * @property userFirstName last message sender first name
 * @property userAvatar last message sender avatar end url
 */
data class ChatHistoryResponse(var chatId: Long, var count: Long, var isBlocked: Boolean, var chatTime: String, var blockedBy: String, var userId: Long, var userFirstName: String, var userAvatar: String)