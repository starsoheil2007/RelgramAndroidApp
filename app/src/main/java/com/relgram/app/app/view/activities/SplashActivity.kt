package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.relgram.app.app.HamsanApp.Companion.context
import com.relgram.app.app.R
import com.relgram.app.app.database.AppDatabases
import com.relgram.app.app.database.Settings
import com.relgram.app.app.databinding.ActivitySplashBinding
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.SplashActivityViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Splash Activity
 *
 */

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashActivityViewModel
    var isFinishedTimer = false
    var isFinishedRest = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this))
                .get(SplashActivityViewModel::class.java)
        binding.viewModel = viewModel
        YoYo.with(Techniques.BounceIn)
                .duration(1000)
                .pivot((binding.logo.width / 2).toFloat(), (binding.logo.height / 2).toFloat())
                .playOn(binding.logo)

        getSettings()

        val handler = Handler()
        handler.postDelayed({ ->
            isFinishedTimer = true
            goToMainPage()
        }, 2000)


    }

    /**
     * get Settings of Application
     *
     */
    fun getSettings() {
        val callRest = WebService().getSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // Progrss Start
                }
                .doOnTerminate {
                    getAppUpdate()
                }
                .subscribe({ result ->
                    if (result.isSuccess && result.listItems != null) {
                        AppDatabases.dbInstance!!.settingsDao()
                                .deleteAll()
                        for (item in result.listItems!!) {
                            var settings = Settings(null, item.name, item.value)
                            AppDatabases.dbInstance!!.settingsDao()
                                    .insert(settings)
                        }
                    }
                }, { error -> print(error.message) })
    }

    /**
     * Check for App Update
     *
     */
    fun getAppUpdate() {
        val callRest = WebService().checkAppUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // Progrss Start
                }
                .doOnTerminate {}
                .subscribe({ result ->
                    if (result.isSuccess && result.item != null) {

                        try {
                            val pInfo = context.getPackageManager()
                                    .getPackageInfo(packageName, 0)
                            val version = pInfo.versionName
                            if (!result.item!!.versionName.equals(version)) {
                                val intent = Intent(this, UpdateActivity::class.java)
                                intent.putExtra(UpdateActivity.VERSION_CODE, result.item!!.versionCode)
                                intent.putExtra(UpdateActivity.VERSION_NAME, result.item!!.versionName)
                                intent.putExtra(UpdateActivity.CHANGE, result.item!!.change)
                                intent.putExtra(UpdateActivity.FORCE_UPDATE, result.item!!.forceUpdate)
                                intent.putExtra(UpdateActivity.DOWNLOAD_LINK, result.item!!.downloadLink)
                                startActivity(intent)
                                finish()
                            } else {
                                isFinishedRest = true
                                goToMainPage()
                            }
                        } catch (e: PackageManager.NameNotFoundException) {
                            isFinishedRest = true
                            goToMainPage()
                        }

                    } else {
                        isFinishedRest = true
                        goToMainPage()
                    }
                }, { error ->
                    print(error.message)
                    isFinishedRest = true
                    goToMainPage()
                })
    }

    /**
     * if call rest and animation has completed go to next page
     * if user has token in local db user is login to app
     * if not go to LoginRegisterActivity for login or register user
     *
     */
    fun goToMainPage() {
        if (isFinishedRest && isFinishedTimer) {
            if (!AppDatabases.getInstance()!!.userInfoDao().getToken().isNullOrEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginRegisterActivity::class.java))
            }
            finish()
        }
    }
}
