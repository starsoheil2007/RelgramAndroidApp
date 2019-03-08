package com.relgram.app.app.models

/**
 * Start new chat to new user
 *
 * @property chatId id of chat
 * @property chatIsBefore these users chat is before
 * @property chatTime time of start
 * @property isBlocked if one of user is blocked is true
 * @property blockedBy blocked by witch user
 */
data class StartChatResponse(var chatId: Long, var chatIsBefore: Boolean, var chatTime: String, var isBlocked: Boolean, var blockedBy: Int)