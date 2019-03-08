package com.relgram.app.app.view.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ListRowContactUsBinding
import com.relgram.app.app.models.ContactUsResponse
import com.relgram.app.app.view_models.ListRowContactUsViewModel

class ContactUsListAdapter : RecyclerView.Adapter<ContactUsListAdapter.ViewHolder>() {


    private lateinit var newsList: List<ContactUsResponse>
    lateinit var binding: ListRowContactUsBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_contact_us, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int {
        return if (::newsList.isInitialized) newsList.size else 0
    }

    fun updatePostList(newsList: List<ContactUsResponse>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding: ListRowContactUsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ListRowContactUsViewModel()

        fun bind(post: ContactUsResponse) {
            viewModel.bind(post)
            binding.viewModel = viewModel
        }
    }
}