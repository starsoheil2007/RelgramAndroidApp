package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivitySendContactBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.library.Toaster
import com.relgram.app.app.view.dialog.LoadingDialog
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.SendContactActivityViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * send new ticket to server
 *
 */
class SendContactActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySendContactBinding
    private lateinit var viewModel: SendContactActivityViewModel
    private lateinit var callRest: Disposable
    public var loading: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_contact)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this)).get(SendContactActivityViewModel::class.java)
        binding.viewModel = viewModel
        binding.sendContactBtn.typeface = FontHelper.instance.getPersianTextTypeface()
        initToolBar()
        loading = LoadingDialog.newInstance(resources.getString(R.string.loading))

        binding.sendContactBtn.setOnClickListener {
            sendContact()
        }
    }

    /**
     * init custom toolbar and custom actions
     *
     */
    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar)
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false)
        binding.include.toolbarTitle.text = resources.getText(R.string.contactUsNew)
        binding.include.iconMessage.visibility = View.GONE
        binding.include.iconUser.visibility = View.GONE
    }

    /**
     * send new ticket to server rest api
     *
     */
    fun sendContact() {
        if (binding.title.text.toString().isNullOrEmpty() || binding.text.text.toString().isNullOrEmpty()) {
            Toaster.toast(this, R.string.pleaseFillAllField)
            return
        }
        callRest = WebService().sendConnectUs(binding.title.text.toString(), binding.text.text.toString(), "1").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (!loading!!.isAdded) loading!!.show(supportFragmentManager, "dialog")
                }
                .doOnTerminate {
                    loading!!.dismiss()
                }
                .subscribe({ result ->
                    if (result.isSuccess) {
                        Toaster.toast(this, R.string.sendInfoSucessfull)
                        finish()
                    }

                }, { error -> print(error.message) })
    }
}
