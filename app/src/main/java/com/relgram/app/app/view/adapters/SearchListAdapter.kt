package com.relgram.app.app.view.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ListRowSearchBinding
import com.relgram.app.app.models.UserRegisterResponse

class SearchListAdapter : RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {


    private lateinit var newsList: List<UserRegisterResponse>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListRowSearchBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_search, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int {
        return if (::newsList.isInitialized) newsList.size else 0
    }

//    fun updatePostList(newsList: List<NewsResponse>) {
//        this.newsList = newsList
//        notifyDataSetChanged()
//    }


    class ViewHolder(private val binding: ListRowSearchBinding) : RecyclerView.ViewHolder(binding.root) {
//        private val viewModel = NewsViewModel()

        fun bind(post: UserRegisterResponse) {
//            viewModel.bind(post)
//            binding.viewModel = viewModel
        }
    }
}