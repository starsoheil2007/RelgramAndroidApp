package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivityChargeNeedBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.ChargeNeedActivityViewModel

/**
 * show message to user for you need become as member ship
 *
 */
class ChargeNeedActivity : AppCompatActivity() {


    private lateinit var binding: ActivityChargeNeedBinding
    private lateinit var viewModel: ChargeNeedActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_charge_need)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this))
                .get(ChargeNeedActivityViewModel::class.java)
        initToolBar()
        binding.buyChargeBtn.typeface = FontHelper.instance.getPersianTextTypeface()
        binding.buyChargeBtn.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar);
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        binding.include.iconUser.visibility = View.GONE
        binding.include.iconMessage.visibility = View.GONE
        binding.include.toolbarTitle.setText(R.string.buyCharge)
    }
}
