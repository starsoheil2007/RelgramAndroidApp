package com.relgram.app.app.view.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.database.Notifications
import com.relgram.app.app.databinding.ListRowNotificationBinding
import com.relgram.app.app.view_models.ListNotitficationViewModel

class NotificationListAdapter : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {


    private lateinit var newsList: List<Notifications>
    lateinit var binding: ListRowNotificationBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_notification, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int {
        return if (::newsList.isInitialized) newsList.size else 0
    }

    fun updatePostList(newsList: List<Notifications>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding: ListRowNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ListNotitficationViewModel()

        fun bind(post: Notifications) {
            viewModel.bind(post)
            binding.viewModel = viewModel
        }
    }
}