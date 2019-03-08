package com.relgram.app.app.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.relgram.app.app.HamsanApp
import com.relgram.app.app.R
import com.relgram.app.app.database.AppDatabases
import com.relgram.app.app.database.UserInfo
import com.relgram.app.app.databinding.FragmentGetActivitionCodeBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.library.Toaster
import com.relgram.app.app.models.BaseResponse
import com.relgram.app.app.models.RegisterRequest
import com.relgram.app.app.view.activities.MainActivity
import com.relgram.app.app.view.dialog.LoadingDialog
import com.relgram.app.app.view.factories.GetActivationCodeFragmentFactory
import com.relgram.app.app.view_models.GetActivationCodeFragmentViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class GetActivitionCodeFragment : Fragment() {

    private lateinit var binding: FragmentGetActivitionCodeBinding
    private lateinit var viewModel: GetActivationCodeFragmentViewModel

    public var registerRequest: RegisterRequest? = null
    public var mobileNumber: String? = null
    private lateinit var callRest: Disposable
    public var loading: LoadingDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_activition_code, container, false)
        viewModel = ViewModelProviders.of(this, GetActivationCodeFragmentFactory(this)).get(GetActivationCodeFragmentViewModel::class.java)
        binding.viewModel = viewModel
        binding.btnFinish.typeface = FontHelper.instance.getPersianTextTypeface()
        if (mobileNumber != null) {
            binding.btnFinish.text = resources.getText(R.string.login)
        }

        binding.btnFinish.setOnClickListener {
            checkCodeAndRegister()
        }
        loading = LoadingDialog.newInstance(resources.getString(R.string.loading))

        return binding.root
    }

    private fun checkCodeAndRegister() {
        if (binding.code.text.isNullOrEmpty()) {
            return
        }
        if (registerRequest != null) {
            registerRequest!!.code = binding.code.text.toString()
            register()
        } else {
            if (mobileNumber != null) {
                login()
            }

        }


    }

    fun register() {
        callRest = WebService().registerUser(registerRequest!!.mobile, registerRequest!!.code, registerRequest!!.firstName, registerRequest!!.lastName, registerRequest!!.sex, registerRequest!!.birthDate!!, registerRequest!!.cityId, registerRequest!!.email, registerRequest!!.aboutMe, registerRequest!!.aboutSpouse, registerRequest!!.avatar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (!loading!!.isAdded)
                        loading!!.show(childFragmentManager, "dialog")
                }
                .doOnTerminate {
                    loading!!.dismiss()
                }
                .subscribe(
                        { result ->
                            if (result.isSuccess) {
                                AppDatabases.getInstance()!!.userInfoDao().insert(userInfo = UserInfo(null, registerRequest!!.firstName + " " + registerRequest!!.lastName, result.item!!.token, registerRequest!!.mobile, registerRequest!!.sex, result.item!!.avatar, result.item!!.userId))
                                startActivity(Intent(activity, MainActivity::class.java))
                                activity!!.finish()
                                HamsanApp.checkForUserDetailsUpdate()
                            }
                        },
                        { error ->
                            try {
                                val errorResult = Gson().fromJson((error as HttpException).response().errorBody()!!.string(), BaseResponse::class.java)
                                if (errorResult.errorCode == 106 || errorResult.errorCode == 105) {
                                    Toaster.toast(this.activity!!, R.string.activationCodeWrong, Toast.LENGTH_LONG)
                                } else if (errorResult.errorCode == 102) {
                                    Toaster.toast(this.activity!!, R.string.mobileRegisterdBefore, Toast.LENGTH_LONG)
                                }
                            } catch (e: Exception) {
                                Toaster.toast(this.activity!!, R.string.registerError, Toast.LENGTH_LONG)
                            }

                        }
                )
    }

    fun login() {
        callRest = WebService().loginUser(this.mobileNumber!!, binding.code.text.toString()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (!loading!!.isAdded)
                        loading!!.show(childFragmentManager, "dialog")
                }
                .doOnTerminate {
                    loading!!.dismiss()
                }
                .subscribe(
                        { result ->
                            if (result.isSuccess) {
                                AppDatabases.getInstance()!!.userInfoDao().insert(userInfo = UserInfo(null, result.item!!.firstName + " " + result.item!!.lastName, result.item!!.token, mobileNumber, result.item!!.sex, result.item!!.avatar, result.item!!.userId))
                                startActivity(Intent(activity, MainActivity::class.java))
                                activity!!.finish()
                                HamsanApp.checkForUserDetailsUpdate()
                            }
                        },
                        { error ->
                            try {
                                val errorResult = Gson().fromJson((error as HttpException).response().errorBody()!!.string(), BaseResponse::class.java)
                                if (errorResult.errorCode == 102) {
                                    Toaster.toast(this.activity!!, R.string.activationCodeWrong, Toast.LENGTH_LONG)
                                } else {
                                    //  TODO: Show Error
                                }
                            } catch (e: Exception) {
                                //  TODO: Show Error
                            }
                        }
                )
    }

    override fun onStop() {
        super.onStop()
        if (::callRest.isInitialized && !callRest.isDisposed) {
            callRest.dispose()
        }
    }
}