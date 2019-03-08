package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivityInfoBinding
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.InfoActivityViewModel

/**
 * About us Activity
 *
 */
class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private lateinit var viewModel: InfoActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_info)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this))
                .get(InfoActivityViewModel::class.java)
        initToolBar()
        // show telegram channel for social network support
        binding.channel.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Relgram_com"))
            startActivity(browserIntent)
        }
    }

    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar);
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        binding.include.iconUser.visibility = View.GONE
        binding.include.iconMessage.visibility = View.GONE
    }
}
