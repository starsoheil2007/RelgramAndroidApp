package com.relgram.app.app.view.adapters

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ChatReceiveListRowBinding
import com.relgram.app.app.databinding.ChatSendListRowBinding
import com.relgram.app.app.models.ChatTextItem
import com.relgram.app.app.view_models.ListChatMessageViewModel

class ChatMessagingListAdapter(var context: Context, var thisUserId: Long) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatList = ArrayList<ChatTextItem>()
    lateinit var bindingSent: ChatSendListRowBinding
    lateinit var bindingRecive: ChatReceiveListRowBinding

    companion object {
        const val VIEW_TYPE_MESSAGE_SENT = 1
        const val VIEW_TYPE_MESSAGE_RECEIVE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            bindingSent = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.chat_send_list_row, parent, false)
            return SendViewHolder(bindingSent)
        } else {
            bindingRecive = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.chat_receive_list_row, parent, false)
            return ReceiveViewHolder(bindingRecive)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == VIEW_TYPE_MESSAGE_SENT) {
            return (holder as SendViewHolder).bind(chatList[position])
        } else {
            return (holder as ReceiveViewHolder).bind(chatList[position])
        }


    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (chatList[position].userId == thisUserId) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVE
        }
    }

    fun updatePostList(chatList: List<ChatTextItem>) {
        this.chatList.addAll(chatList)
        notifyDataSetChanged()
    }


    class SendViewHolder(private val bindingSent: ChatSendListRowBinding) : RecyclerView.ViewHolder(bindingSent.root) {
        private val viewModel = ListChatMessageViewModel()
        fun bind(chat: ChatTextItem) {
            viewModel.bind(chat)
            bindingSent.viewModel = viewModel
        }
    }

    class ReceiveViewHolder(private val bindingRecive: ChatReceiveListRowBinding) : RecyclerView.ViewHolder(bindingRecive.root) {
        private val viewModel = ListChatMessageViewModel()
        fun bind(chat: ChatTextItem) {
            viewModel.bind(chat)
            bindingRecive.viewModel = viewModel
        }
    }

}