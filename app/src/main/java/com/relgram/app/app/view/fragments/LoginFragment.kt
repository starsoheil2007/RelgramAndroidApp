package com.relgram.app.app.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.relgram.app.app.R
import com.relgram.app.app.database.AppDatabases
import com.relgram.app.app.databinding.FragmentLoginBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.models.BaseResponse
import com.relgram.app.app.view.activities.LoginRegisterActivity
import com.relgram.app.app.view.dialog.LoadingDialog
import com.relgram.app.app.view.factories.LoginFragmentFactory
import com.relgram.app.app.view_models.LoginFragmentViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class LoginFragment : Fragment() {


    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginFragmentViewModel
    private lateinit var callRest: Disposable
    public var loading: LoadingDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModel = ViewModelProviders.of(this, LoginFragmentFactory(this)).get(LoginFragmentViewModel::class.java)


        val settingText = AppDatabases.dbInstance!!.settingsDao().getById("2")
        if (settingText != null) {
            viewModel.bind(settingText.value)
        }
        binding.viewModel = viewModel
        loading = LoadingDialog.newInstance(resources.getString(R.string.loading))



        binding.btnLogin.typeface = FontHelper.instance.getPersianTextTypeface()

        binding.btnLogin.setOnClickListener {
            checkNumberExist()
        }

        return binding.root
    }

    private fun checkNumberExist() {
        if (binding.mobileNumber.text.isNullOrEmpty() || !isValidate(binding.mobileNumber.text.toString())) {
            return
        }
        callRest = WebService().existMobileNumber(binding.mobileNumber.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (!loading!!.isAdded)
                        loading!!.show(childFragmentManager, "dialog")
                }
                .doOnTerminate {
                }
                .subscribe(
                        { result ->
                            if (result.errorCode == 100) { // Go To Login
                                getActivationCode()
                            }
                        },
                        { error ->
                            loading!!.dismiss()
                            try {
                                var errorResult = Gson().fromJson((error as HttpException).response().errorBody()!!.string(), BaseResponse::class.java)
                                if (errorResult.errorCode == 101) {
                                    (activity as LoginRegisterActivity).showRolesFragment()
                                } else {
                                    //  TODO: Show Error
                                }
                            } catch (e: Exception) {
                                //  TODO: Show Error
                            }
                        }
                )
    }

    private fun isValidate(number: String?): Boolean {
        if (number != null) {
            if (android.util.Patterns.PHONE.matcher(number).matches()) {
                return true
            }
        }
        return false
    }


    override fun onStop() {
        super.onStop()
        if (::callRest.isInitialized && !callRest.isDisposed) {
            callRest.dispose()
        }
    }

    fun getActivationCode() {
        callRest = WebService().getActivationCode(binding.mobileNumber.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                }
                .doOnTerminate {
                    loading!!.dismiss()
                }
                .subscribe(
                        { result ->
                            loading!!.dismiss()
                            if (result.errorCode == 100) { // Go To Login
                                (activity as LoginRegisterActivity).showCodeFragment(mobileNumber = binding.mobileNumber.text.toString())
                            }
                        },
                        { error ->
                            loading!!.dismiss()
                            try {
                                var errorResult = Gson().fromJson((error as HttpException).response().errorBody()!!.string(), BaseResponse::class.java)
                                if (errorResult.errorCode == 101) {
                                    (activity as LoginRegisterActivity).showRegisterFragment()
                                } else {
                                    //  TODO: Show Error
                                }
                            } catch (e: Exception) {
                                //  TODO: Show Error
                            }
                        }
                )
    }
}