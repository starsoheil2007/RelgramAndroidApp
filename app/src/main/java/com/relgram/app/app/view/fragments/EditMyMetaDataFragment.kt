package com.relgram.app.app.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.databinding.FragmentEditMetaDataBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.library.Toaster
import com.relgram.app.app.view.adapters.MetaDataListAdapter
import com.relgram.app.app.view.factories.FavoriteFragmentFactory
import com.relgram.app.app.view_models.EditMetaDataFragmentViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EditMyMetaDataFragment : Fragment() {

    private lateinit var callRest: Disposable
    lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: FragmentEditMetaDataBinding
    private lateinit var viewModel: EditMetaDataFragmentViewModel
    lateinit var metaDataListAdapter: MetaDataListAdapter
    private var selectedItems: HashMap<Int, String> = HashMap()
    public var otherUserId: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_meta_data, container, false)
        viewModel = ViewModelProviders.of(this, FavoriteFragmentFactory(this)).get(EditMetaDataFragmentViewModel::class.java)
        binding.viewModel = viewModel
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.mainList.layoutManager = layoutManager
        metaDataListAdapter = MetaDataListAdapter(this.activity!!, otherUserId != 0.toLong())
        binding.mainList.adapter = metaDataListAdapter
        getMetaDataList()

        if (otherUserId == 0.toLong()) {
            binding.registerBtn.typeface = FontHelper.instance.getPersianTextTypeface()
            binding.registerBtn.setOnClickListener {
                if (metaDataListAdapter.getSelectedMeta().isNullOrEmpty()) {
                    return@setOnClickListener
                }
                addMetaData(metaDataListAdapter.getSelectedMeta())
            }
        } else {
            binding.registerBtn.visibility = View.GONE
        }

        return binding.root
    }


    fun getMetaDataList() {
        callRest = WebService().getMetaData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                }.doOnTerminate {

                }.subscribe(
                        { result ->
                            if (result.isSuccess && result.listItems != null) {
                                metaDataListAdapter.updatePostList(result.listItems!!)
                                getMyMetaValues()
                            }

                        },
                        { error -> }
                )
    }

    fun addMetaData(meValues: String) {
        callRest = WebService().addMetaValues(meValues, "{}").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                }.doOnTerminate {

                }.subscribe(
                        { result ->
                            if (result.isSuccess) {
                                Toaster.toast(this.activity!!, R.string.sendInfoSucessfull)
                            }

                        },
                        { error -> }
                )
    }

    fun getMyMetaValues() {
        callRest = WebService().getMetaValues(otherUserId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                }.doOnTerminate {

                }.subscribe(
                        { result ->
                            if (result.isSuccess) {
                                if (result.item!!.myMetaData.isNotEmpty()) {
                                    for (i in result.item!!.myMetaData) {
                                        selectedItems.put(i.metaValueId, i.metaValueSelected)
                                    }
                                    metaDataListAdapter.updateSelectedList(selectedItems)
                                }

                            }

                        },
                        { error -> }
                )
    }

}