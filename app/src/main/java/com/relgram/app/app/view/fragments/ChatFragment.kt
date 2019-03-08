package com.relgram.app.app.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.databinding.FragmentChatBinding
import com.relgram.app.app.view.adapters.ChatHistoryListAdapter
import com.relgram.app.app.view.factories.ChatFragmentFactory
import com.relgram.app.app.view_models.ChatFragmentViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var viewModel: ChatFragmentViewModel
    lateinit var handler: Handler
    private lateinit var callRest: Disposable
    lateinit var layoutManager: LinearLayoutManager
    var page: Int = 0
    lateinit var adapter: ChatHistoryListAdapter
    public var isLastPage = false

    companion object {
        val PAGE_SIZE = 12
    }

    var runnable: Runnable? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        viewModel = ViewModelProviders.of(this, ChatFragmentFactory(this)).get(ChatFragmentViewModel::class.java)
        binding.viewModel = viewModel
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.mainList.layoutManager = layoutManager

        handler = Handler()
        runnable = Runnable {
            getChatHistory()
        }

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
                        getChatHistory()
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })


        return binding.root
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            getChatHistory()
        } else {
            if (::handler.isInitialized) handler.removeCallbacks(runnable)
        }
    }

    override fun onResume() {
        super.onResume()
        page = 0
    }



    fun getChatHistory() {
        callRest = WebService().chatHistory(page)
                .subscribeOn(Schedulers.io())
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
                    handler.postDelayed(runnable, 5000)
                }
                .subscribe(
                        { result ->
                            if (result.listItems!!.isEmpty()) {
                                isLastPage = true
                            } else {
                                if (result.listItems!!.size < PAGE_SIZE) {
                                    isLastPage = true
                                }
                                if (page != 0) {
                                    adapter.updatePostList(result.listItems!!)
                                } else {
                                    adapter = ChatHistoryListAdapter(this.activity!!)
                                    adapter.updatePostList(result.listItems!!)
                                    binding.mainList.adapter = adapter
                                }
                            }

                        },
                        { error -> print(error.message) }
                )

    }

    override fun onStop() {
        super.onStop()
        if (::callRest.isInitialized && !callRest.isDisposed)
            callRest.dispose()
    }


}