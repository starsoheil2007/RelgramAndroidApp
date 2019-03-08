package com.relgram.app.app.view.adapters

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ListRowChatHistoryBinding
import com.relgram.app.app.models.ChatHistoryResponse
import com.relgram.app.app.view.activities.ChatMessagingActivity
import com.relgram.app.app.view_models.ListChatHistoryViewModel

class ChatHistoryListAdapter(var context: Context) : RecyclerView.Adapter<ChatHistoryListAdapter.ViewHolder>() {


    private lateinit var chatList: List<ChatHistoryResponse>
    lateinit var binding: ListRowChatHistoryBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_chat_history, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chatList[position])
        binding.root.setOnClickListener {
            val intent = Intent(context, ChatMessagingActivity::class.java)
            intent.putExtra(ChatMessagingActivity.CHAT_ID, chatList[position].chatId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return if (::chatList.isInitialized) chatList.size else 0
    }

    fun updatePostList(newsList: List<ChatHistoryResponse>) {
        this.chatList = newsList
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding: ListRowChatHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ListChatHistoryViewModel()

        fun bind(chat: ChatHistoryResponse) {
            viewModel.bind(chat)
            binding.viewModel = viewModel
        }
    }
}