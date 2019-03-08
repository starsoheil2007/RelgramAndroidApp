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
import com.relgram.app.app.databinding.FragmentSearchBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.library.Toaster
import com.relgram.app.app.view.adapters.ProvinceCitySpinnerAdapter
import com.relgram.app.app.view.adapters.UsersListAdapter
import com.relgram.app.app.view.factories.SearchFragmentFactory
import com.relgram.app.app.view_models.SearchFragmentViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchFragment : Fragment() {


    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchFragmentViewModel
    private lateinit var callRest: Disposable
    lateinit var adapter: UsersListAdapter
    lateinit var layoutManager: LinearLayoutManager

    public var cityId: Int = 0
    var page: Int = 0
    public var isLastPage = false

    companion object {
        val PAGE_SIZE = 12
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        viewModel = ViewModelProviders.of(this, SearchFragmentFactory(this)).get(SearchFragmentViewModel::class.java)
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.mainList.layoutManager = layoutManager
        binding.viewModel = viewModel

        getProvinceList()

        viewModel.onProvinceSelectedListener = object : SearchFragmentViewModel.onProvinceSelected {
            override fun onSelected(provinceId: Long) {
                getCityOfProvince(provinceId)
            }
        }

        viewModel.onCitySelectedListener = object : SearchFragmentViewModel.onCitySelected {
            override fun onSelected(cityId: Long) {
                page = 0
                this@SearchFragment.cityId = cityId.toInt()
            }
        }

        binding.searchBtn.setOnClickListener {
            if (cityId == 0) {
                Toaster.toast(this.activity!!, R.string.pleaseSelectCity)
                return@setOnClickListener
            }

            searchUser()
        }
        binding.searchBtn.typeface = FontHelper.instance.getIconTypeface()

        binding.mainList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (callRest.isDisposed && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= HomeFragment.PAGE_SIZE) {
                        page++
                        searchUser()
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        return binding.root
    }

    fun getProvinceList() {
        callRest = WebService().getProvince()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                }
                .doOnTerminate {
                }
                .subscribe(
                        { result ->
                            if (result.isSuccess) {
                                binding.province.adapter = ProvinceCitySpinnerAdapter(result.listItems!!)
                            }
                        },
                        { error ->
                        }
                )
    }

    fun getCityOfProvince(provinceId: Long) {
        callRest = WebService().getCity(provinceId.toInt()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
        }.doOnTerminate {
        }.subscribe(
                { result ->
                    if (result.isSuccess) {
                        binding.city.adapter = ProvinceCitySpinnerAdapter(result.listItems!!)
                        if (result.listItems!!.isNotEmpty()) {
                            if (::adapter.isInitialized) {
                                adapter.clear()
                            }
                            cityId = result.listItems!![0].id
                        }
                    }

                },
                { error ->
                }
        )
    }

    override fun onStop() {
        super.onStop()
        if (::callRest.isInitialized && !callRest.isDisposed) {
            callRest.dispose()
        }
    }


    fun searchUser() {
        callRest = WebService().search(page, cityId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    binding.notFound.visibility = View.GONE
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
                            if (result.listItems!!.isEmpty() && page != 0) {
                                isLastPage = true
                            } else {
                                if (result.listItems!!.size < PAGE_SIZE) {
                                    isLastPage = true
                                }
                                if (page != 0) {
                                    adapter.updatePostList(userList = result.listItems!!)
                                } else {
                                    if (result.listItems!!.isEmpty()) {
                                        if (::adapter.isInitialized) {
                                            adapter.clear()
                                        }
                                        binding.notFound.visibility = View.VISIBLE
                                    }
                                    adapter = UsersListAdapter(this.activity!!)
                                    adapter.updatePostList(userList = result.listItems!!)
                                    binding.mainList.adapter = adapter
                                }
                            }
                        },
                        { error -> print(error.message) }
                )
    }


}