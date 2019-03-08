package com.relgram.app.app.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.database.AppDatabases
import com.relgram.app.app.databinding.FragmentUserBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.view.activities.PaymentActivity
import com.relgram.app.app.view.factories.UserFragmentFactory
import com.relgram.app.app.view_models.UserFragmentViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class UserFragment : Fragment() {


    private lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: UserFragmentViewModel
    private lateinit var callRest: Disposable


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        viewModel = ViewModelProviders.of(this, UserFragmentFactory(this)).get(UserFragmentViewModel::class.java)
        binding.viewModel = viewModel
        getMyInfo()
        binding.buyChargeBtn.typeface = FontHelper.instance.getPersianTextTypeface()

        binding.buyChargeBtn.setOnClickListener {
            val intent = Intent(activity, PaymentActivity::class.java)
            activity!!.startActivity(intent)
        }


        var settings = AppDatabases.dbInstance!!.settingsDao().getById("5")
        if (settings != null) {
            binding.userPageText.text = settings.value
        }


        return binding.root
    }


    private fun getMyInfo() {
        callRest = WebService().getMyInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // Progrss Start
                }
                .doOnTerminate {
                    // Progress End
                }
                .subscribe(
                        { result ->
                            if (result.isSuccess && result.item != null) {
                                viewModel.bind(result.item!!)
                                binding.viewModel = viewModel
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