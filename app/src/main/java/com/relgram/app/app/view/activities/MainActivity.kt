package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivityMainBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.view.adapters.MainFragmentAdapter
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.MainActivityViewModel

/**
 * show main activity
 * This app has bottom toolbar
 *
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    companion object {
        const val FINISH_APP_REQUEST_CODE = 1009
        const val FINISH_APP_RESULT_CODE = 1010
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this)).get(MainActivityViewModel::class.java)

        binding.pager.adapter = MainFragmentAdapter(supportFragmentManager)
        setBottomTabActions()
        binding.centerHomeKey.typeface = FontHelper.instance.getIconTypeface()
        selectTab(1)
        initToolBar()
    }

    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar);
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        binding.include.iconMessage.visibility = View.VISIBLE
        binding.include.iconUser.text = resources.getString(R.string.iconSettings)
        binding.include.iconUser.setOnClickListener {
            startActivityForResult(Intent(this, SettingsActivity::class.java), FINISH_APP_REQUEST_CODE)
        }
        binding.include.iconMessage.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
    }


    /**
     * Change color and set view pager item
     *
     */
    fun setBottomTabActions() {

        binding.profileTab.setOnClickListener { view ->
            binding.pager.currentItem = 0
            selectTab(1)
        }

        binding.searchTab.setOnClickListener { view ->
            binding.pager.currentItem = 1
            selectTab(2)
        }

        binding.centerHomeKey.setOnClickListener { view ->
            binding.pager.currentItem = 2
            selectTab(3)
        }

        binding.favoriteTab.setOnClickListener { view ->
            binding.pager.currentItem = 3
            selectTab(4)
        }

        binding.chatTab.setOnClickListener { view ->
            binding.pager.currentItem = 4
            selectTab(5)
        }

    }

    /**
     * Change Color of selected tab to gray
     *
     * @param witch witch item is selected
     */
    fun selectTab(witch: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.profileText.setTextColor(resources.getColor(R.color.white, theme))
            binding.chatText.setTextColor(resources.getColor(R.color.white, theme))
            binding.searchText.setTextColor(resources.getColor(R.color.white, theme))
            binding.favoriteText.setTextColor(resources.getColor(R.color.white, theme))
            binding.chatText.setTextColor(resources.getColor(R.color.white, theme))
            binding.centerHomeKey.setTextColor(resources.getColor(R.color.white, theme))
        } else {
            binding.profileText.setTextColor(resources.getColor(R.color.white))
            binding.chatText.setTextColor(resources.getColor(R.color.white))
            binding.searchText.setTextColor(resources.getColor(R.color.white))
            binding.favoriteText.setTextColor(resources.getColor(R.color.white))
            binding.chatText.setTextColor(resources.getColor(R.color.white))
            binding.centerHomeKey.setTextColor(resources.getColor(R.color.white))

        }
        when (witch) {
            1 -> {
                binding.include.toolbarTitle.text = resources.getText(R.string.tabProfile)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.profileText.setTextColor(resources.getColor(R.color.selectedTabColor, theme))
                } else {
                    binding.profileText.setTextColor(resources.getColor(R.color.selectedTabColor))
                }
            }

            2 -> {
                binding.include.toolbarTitle.text = resources.getText(R.string.tabSearch)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.searchText.setTextColor(resources.getColor(R.color.selectedTabColor, theme))
                } else {
                    binding.searchText.setTextColor(resources.getColor(R.color.selectedTabColor))
                }
            }

            3 -> {
                binding.include.toolbarTitle.text = resources.getText(R.string.tabUsers)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.centerHomeKey.setTextColor(resources.getColor(R.color.selectedTabColor, theme))
                } else {
                    binding.centerHomeKey.setTextColor(resources.getColor(R.color.selectedTabColor))
                }
            }

            4 -> {
                binding.include.toolbarTitle.text = resources.getText(R.string.tabFavorites)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.favoriteText.setTextColor(resources.getColor(R.color.selectedTabColor, theme))
                } else {
                    binding.favoriteText.setTextColor(resources.getColor(R.color.selectedTabColor))
                }
            }

            5 -> {
                binding.include.toolbarTitle.text = resources.getText(R.string.tabChat)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.chatText.setTextColor(resources.getColor(R.color.selectedTabColor, theme))
                } else {
                    binding.chatText.setTextColor(resources.getColor(R.color.selectedTabColor))
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FINISH_APP_REQUEST_CODE) {
            if (resultCode == FINISH_APP_RESULT_CODE) {
                finish();
            }
        }
    }

}
