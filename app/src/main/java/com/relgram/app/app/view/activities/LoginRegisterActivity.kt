package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivityLoginRegisterBinding
import com.relgram.app.app.models.RegisterRequest
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view.fragments.GetActivitionCodeFragment
import com.relgram.app.app.view.fragments.LoginFragment
import com.relgram.app.app.view.fragments.RegisterFragment
import com.relgram.app.app.view.fragments.RolesAcceptFragment
import com.relgram.app.app.view_models.LoginRegisterActivityViewModel

/**
 * Login register Activity
 * user follow : showNumberFragment if user registered => showCodeFragment
 * if not registered => showRolesFragment => showRegisterFragment => showCodeFragment
 */
class LoginRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginRegisterBinding
    private lateinit var viewModel: LoginRegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_register)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this))
                .get(LoginRegisterActivityViewModel::class.java)

        showNumberFragment()

    }

    /**
     * show number fragment for get mobile number
     *
     */
    private fun showNumberFragment() {
        var number = LoginFragment()
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.add(R.id.mainFrame, number, "number")
        ft.commit()
    }

    /**
     * show roles fragment for accept app roles by user
     *
     */
    public fun showRolesFragment() {
        var rolesFragment = RolesAcceptFragment()
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.replace(R.id.mainFrame, rolesFragment, "roles")
        ft.commit()
    }

    /**
     * show register fragment for get user info from user
     *
     */
    public fun showRegisterFragment() {
        var registerFragment = RegisterFragment()
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.replace(R.id.mainFrame, registerFragment, "register")
        ft.commit()
    }

    /**
     * show code fragment in register method for get code that text by sms
     *
     * @param registerRequest
     */
    public fun showCodeFragment(registerRequest: RegisterRequest) {
        var registerFragment = GetActivitionCodeFragment()
        registerFragment.registerRequest = registerRequest
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.replace(R.id.mainFrame, registerFragment, "register")
        ft.commit()
    }

    /**
     * show code fragment in login method for get code that text by sms
     *
     * @param mobileNumber
     */
    public fun showCodeFragment(mobileNumber: String) {
        var registerFragment = GetActivitionCodeFragment()
        registerFragment.mobileNumber = mobileNumber
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.replace(R.id.mainFrame, registerFragment, "register")
        ft.commit()
    }


}
