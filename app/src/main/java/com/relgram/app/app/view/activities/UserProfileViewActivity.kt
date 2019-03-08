package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivityUserProfileViewBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.view.adapters.SliderAdapter
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.UserProfileViewActivityViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Thi activity is for show user details
 *
 */
class UserProfileViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileViewBinding
    private lateinit var viewModel: UserProfileViewActivityViewModel
    var userId: Long = 0
    private lateinit var callRest: Disposable

    companion object {
        val USER_ID = "userId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile_view)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this)).get(UserProfileViewActivityViewModel::class.java)
        binding.viewModel = viewModel
        binding.likeBtn.typeface = FontHelper.instance.getIconTypeface()
        binding.chatBtn.typeface = FontHelper.instance.getIconTypeface()
        binding.moreBtn.typeface = FontHelper.instance.getIconTypeface()

        initToolBar()

        if (intent.extras.getLong(USER_ID, 0) != 0.toLong()) {
            userId = intent.extras.getLong(USER_ID)
        }
        getUserInfo()
    }


    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar);
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        binding.include.iconMessage.visibility = View.GONE
        binding.include.iconUser.visibility = View.GONE
        binding.include.toolbarTitle.setText(R.string.userDetails)

    }

    fun getUserInfo() {
        callRest = WebService().getUserInfoForShow(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // Progrss Start
                }
                .doOnTerminate {
                    // Progress End
                }
                .subscribe({ result ->
                    if (result.isSuccess && result.item != null) {
                        viewModel.bind(result.item!!, this)
                        binding.viewModel = viewModel
                        binding.sliderPager.adapter = SliderAdapter(this@UserProfileViewActivity, imageList = result.item!!.images, isBanner = false)
                    }
                }, { error -> print(error.message) })
    }

    override fun onStop() {
        super.onStop()
        if (::callRest.isInitialized && !callRest.isDisposed) callRest.dispose()
    }

}
