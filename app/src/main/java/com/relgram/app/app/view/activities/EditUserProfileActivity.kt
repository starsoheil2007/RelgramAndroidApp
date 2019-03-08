package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivityEditProfileBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view.fragments.EditBaseInfoFragment
import com.relgram.app.app.view.fragments.EditMyMetaDataFragment
import com.relgram.app.app.view.fragments.EditSpouseMetaDataFragment
import com.relgram.app.app.view.fragments.GalleryFragment
import com.relgram.app.app.view_models.EditProfileActivityViewModel

/**
 * Edit user profile activity
 *
 */
class EditUserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this)).get(EditProfileActivityViewModel::class.java)
        initToolBar()

        binding.baseInfo.typeface = FontHelper.instance.getPersianTextTypeface()
        binding.myMetaData.typeface = FontHelper.instance.getPersianTextTypeface()
        binding.spouseMetaData.typeface = FontHelper.instance.getPersianTextTypeface()
        binding.gallery.typeface = FontHelper.instance.getPersianTextTypeface()

        //        this segmented button show 4 fragment by changing
        binding.segmentedView.setOnCheckedChangeListener { p0, p1 ->
            if (p1 == R.id.baseInfo) {
                showFragment(EditBaseInfoFragment())
            } else if (p1 == R.id.myMetaData) {
                showFragment(EditMyMetaDataFragment())
            } else if (p1 == R.id.spouseMetaData) {
                showFragment(EditSpouseMetaDataFragment())
            } else {
                showFragment(GalleryFragment())

            }
        }
        //        first fragment
        showFragment(EditBaseInfoFragment())
    }

    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar);
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        binding.include.iconMessage.visibility = View.VISIBLE
        binding.include.iconMessage.visibility = View.GONE
    }

    /**
     * Replace fragment
     *
     * @param fragment
     */
    public fun showFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.replace(R.id.mainFrame, fragment, "register")
        ft.commit()
    }


}
