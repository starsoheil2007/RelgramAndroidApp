package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.relgram.app.app.HamsanApp.Companion.context
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivityContactBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.view.adapters.ContactUsListAdapter
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.ContactActivityViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * show older Ticket activity
 *
 */
class ContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactBinding
    private lateinit var viewModel: ContactActivityViewModel
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: ContactUsListAdapter
    public var page = 0
    private lateinit var callRest: Disposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this)).get(ContactActivityViewModel::class.java)
        binding.viewModel = viewModel
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.mainList.layoutManager = layoutManager
        adapter = ContactUsListAdapter()
        binding.mainList.adapter = adapter
        getContactList()
        initToolBar()

        //        add new ticket action
        binding.addKey.typeface = FontHelper.instance.getIconTypeface()
        binding.addKey.setOnClickListener {
            val intent = Intent(context, SendContactActivity::class.java)
            context.startActivity(intent)
        }


    }

    /**
     * get older ticket from server by rest api
     *
     */
    fun getContactList() {
        callRest = WebService().getConnectUs(page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {}
                .doOnTerminate {}
                .subscribe({ result ->
                    if (result.isSuccess && result.listItems != null) {
                        adapter.updatePostList(result.listItems!!)
                    }

                }, { error -> print(error.message) })
    }

    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar)
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false)
        binding.include.toolbarTitle.text = resources.getText(R.string.contactUsList)
        binding.include.iconMessage.visibility = View.GONE
        binding.include.iconUser.visibility = View.GONE
    }


}
