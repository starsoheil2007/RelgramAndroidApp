package com.relgram.app.app.view.adapters

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ListRowUserBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.models.UserDetailsResponse
import com.relgram.app.app.view_models.ListUserViewModel

class UsersListAdapter(var context: Context) : RecyclerView.Adapter<UsersListAdapter.ViewHolder>() {


    private var userList = ArrayList<UserDetailsResponse>()
    lateinit var binding: ListRowUserBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_user, parent, false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
        binding.showInfoBtn.typeface = FontHelper.instance.getPersianTextTypeface()
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updatePostList(userList: List<UserDetailsResponse>) {
        this.userList.addAll(userList)
        notifyDataSetChanged()
    }

    fun clear() {
        this.userList = ArrayList<UserDetailsResponse>()
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding: ListRowUserBinding, var context: Context) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ListUserViewModel()
        fun bind(onUser: UserDetailsResponse) {
            viewModel.bind(onUser, context = context)
            binding.viewModel = viewModel
        }
    }
}