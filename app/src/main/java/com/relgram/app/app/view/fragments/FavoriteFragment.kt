package com.relgram.app.app.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.databinding.FragmentFavoriteBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.view.adapters.FavoriteListAdapter
import com.relgram.app.app.view.factories.FavoriteFragmentFactory
import com.relgram.app.app.view_models.FavoriteFragmentViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FavoriteFragment : Fragment() {


    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavoriteFragmentViewModel
    var pageFavMe: Int = 0
    var pageMe: Int = 0
    private lateinit var callRest: Disposable
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: FavoriteListAdapter

    public var isLastPage = false

    companion object {
        val PAGE_SIZE = 12
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        viewModel = ViewModelProviders.of(this, FavoriteFragmentFactory(this)).get(FavoriteFragmentViewModel::class.java)
        binding.viewModel = viewModel
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.mainList.layoutManager = layoutManager
        binding.followedMe.typeface = FontHelper.instance.getPersianTextTypeface()
        binding.meFollowThose.typeface = FontHelper.instance.getPersianTextTypeface()
        getMyFav()
        binding.segmentedView.setOnCheckedChangeListener { p0, p1 ->
            if (p1 == R.id.meFollowThose) {
                pageMe = 0
                getMyFav()
            } else {
                pageFavMe = 0
                getFaveMe()
            }
        }

        binding.mainList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (callRest.isDisposed && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        if (binding.segmentedView.checkedRadioButtonId == R.id.meFollowThose) {
                            pageMe++
                            getMyFav()
                        } else {
                            pageFavMe++
                            getFaveMe()
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })


        return binding.root
    }

    fun getFaveMe() {
        callRest = WebService().getUserFavMe(pageFavMe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (pageFavMe == 0) {
                        binding.progressCircular.visibility = View.VISIBLE
                    } else {
                        binding.moreItemProgress.visibility = View.VISIBLE
                    }
                }.doOnTerminate {
                    if (pageFavMe == 0) {
                        binding.progressCircular.visibility = View.GONE
                    } else {
                        binding.moreItemProgress.visibility = View.GONE
                    }
                }.subscribe(
                        { result ->
                            if (result.listItems!!.isEmpty()) {
                                isLastPage = true
                            } else {
                                if (result.listItems!!.size < PAGE_SIZE) {
                                    isLastPage = true
                                }
                                if (pageFavMe != 0) {
                                    adapter.updatePostList(result.listItems!!)
                                } else {
                                    adapter = FavoriteListAdapter(this.activity!!)
                                    adapter.updatePostList(result.listItems!!)
                                    binding.mainList.adapter = adapter
                                }
                            }
                        },
                        { error -> }
                )
    }


    fun getMyFav() {
        callRest = WebService().getMyUserFav(pageMe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (pageMe == 0) {
                        binding.progressCircular.visibility = View.VISIBLE
                    } else {
                        binding.moreItemProgress.visibility = View.VISIBLE
                    }
                }.doOnTerminate {
                    if (pageMe == 0) {
                        binding.progressCircular.visibility = View.GONE
                    } else {
                        binding.moreItemProgress.visibility = View.GONE
                    }

                }.subscribe(
                        { result ->
                            if (result.listItems!!.isEmpty()) {
                                isLastPage = true
                            } else {
                                if (result.listItems!!.size < PAGE_SIZE) {
                                    isLastPage = true
                                }
                                if (pageMe != 0) {
                                    adapter.updatePostList(result.listItems!!)
                                } else {
                                    adapter = FavoriteListAdapter(this.activity!!)
                                    adapter.updatePostList(result.listItems!!)
                                    binding.mainList.adapter = adapter
                                }
                            }
                        },
                        { error -> }
                )
    }

    override fun onStop() {
        super.onStop()
        if (::callRest.isInitialized && !callRest.isDisposed)
            callRest.dispose()
    }

}