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
import com.relgram.app.app.databinding.FragmentHomeBinding
import com.relgram.app.app.view.adapters.SliderAdapter
import com.relgram.app.app.view.adapters.UsersListAdapter
import com.relgram.app.app.view.factories.HomeFragmentFactory
import com.relgram.app.app.view_models.HomeFragmentViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    var page: Int = 0
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: UsersListAdapter
    private lateinit var callRest: Disposable
    public var isLastPage = false

    companion object {
        val PAGE_SIZE = 12
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProviders.of(this, HomeFragmentFactory(this)).get(HomeFragmentViewModel::class.java)
        binding.viewModel = viewModel
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.mainList.layoutManager = layoutManager

        binding.mainList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (callRest.isDisposed && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        page++
                        getLastUsers()
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        page = 0
        getLastUsers()

    }

    fun getLastUsers() {
        callRest = WebService().getLastUser(page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (page == 0) {
                        binding.progressCircular.visibility = View.VISIBLE
                    } else {
                        binding.moreItemProgress.visibility = View.VISIBLE
                    }
                }
                .doOnTerminate {
                    if (page == 0) {
                        binding.progressCircular.visibility = View.GONE
                    } else {
                        binding.moreItemProgress.visibility = View.GONE
                    }
                }
                .subscribe(
                        { result ->
                            if (result.listItems!!.isEmpty()) {
                                isLastPage = true
                            } else {
                                if (result.listItems!!.size < 12) {
                                    isLastPage = true
                                }
                                if (page != 0) {
                                    adapter.updatePostList(userList = result.listItems!!)
                                } else {
                                    adapter = UsersListAdapter(this.activity!!)
                                    adapter.updatePostList(userList = result.listItems!!)
                                    binding.mainList.adapter = adapter
                                    getBanners()
                                }
                            }
                        },
                        { error -> print(error.message) }
                )
    }


    fun getBanners() {
        callRest = WebService().getBanners().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                }
                .doOnTerminate {

                }
                .subscribe(
                        { result ->
                            if (result.isSuccess && result.listItems != null) {
                                var imageList: MutableList<String> = ArrayList<String>()
                                for (item in result.listItems!!) {
                                    if (!item.bannerImage.isNullOrEmpty()) {
                                        imageList.add(item.bannerImage)
                                    }
                                }
                                binding.sliderPager.adapter = SliderAdapter(this.activity!!, imageList = imageList, isBanner = true)


                            }

                        },
                        { error ->
                            print(error.message)
                        }
                )

    }

    override fun onStop() {
        super.onStop()
        if (::callRest.isInitialized && !callRest.isDisposed)
            callRest.dispose()
    }
}