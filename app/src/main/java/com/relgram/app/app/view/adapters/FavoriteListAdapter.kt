package com.relgram.app.app.view.adapters

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ListRowFavoriteBinding
import com.relgram.app.app.library.Toaster
import com.relgram.app.app.models.MyFavUser
import com.relgram.app.app.view.activities.UserProfileViewActivity
import com.relgram.app.app.view_models.ListFavoriteViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FavoriteListAdapter(val context: Context) : RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {


    private lateinit var favList: List<MyFavUser>
    lateinit var binding: ListRowFavoriteBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_favorite, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favList[position])
        binding.deleteFav.setOnClickListener { view ->
            if (view.tag != 0)
                deleteUserFav(id = view.tag as Long)
        }

        binding.root.setOnClickListener {
            val intent = Intent(context, UserProfileViewActivity::class.java)
            intent.putExtra(UserProfileViewActivity.USER_ID, favList[position].userId)
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (::favList.isInitialized) favList.size else 0
    }

    fun updatePostList(newsList: List<MyFavUser>) {
        this.favList = newsList
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding: ListRowFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ListFavoriteViewModel()

        fun bind(post: MyFavUser) {
            viewModel.bind(post)
            binding.viewModel = viewModel
        }
    }

    fun deleteUserFav(id: Long) {
//        Todo : Question and refresh listenre
        val callRest = WebService().deleteUserFav(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                }.doOnTerminate {

                }.subscribe(
                        { result ->
                            if (result.isSuccess) {
                                Toaster.toast(context, R.string.sendInfoSucessfull)
                            }
                        },
                        { error -> }
                )
    }
}