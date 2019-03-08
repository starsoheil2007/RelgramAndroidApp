package com.relgram.app.app.view.fragments

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.mojtaba.materialdatetimepicker.date.DatePickerDialog
import com.mojtaba.materialdatetimepicker.utils.PersianCalendar
import com.relgram.app.app.R
import com.relgram.app.app.databinding.FragmentRegisterBinding
import com.relgram.app.app.library.CalendarTool
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.library.ImageLoaderHelper
import com.relgram.app.app.library.Toaster
import com.relgram.app.app.models.BaseResponse
import com.relgram.app.app.models.RegisterRequest
import com.relgram.app.app.view.activities.LoginRegisterActivity
import com.relgram.app.app.view.adapters.ProvinceCitySpinnerAdapter
import com.relgram.app.app.view.dialog.LoadingDialog
import com.relgram.app.app.view.factories.RegisterFragmentFactory
import com.relgram.app.app.view_models.RegisterFragmentViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.File

class RegisterFragment : Fragment(), DatePickerDialog.OnDateChangedListener, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterFragmentViewModel
    private var year: Int? = null
    private var month: Int? = null
    private var day: Int? = null
    private lateinit var callRest: Disposable
    public var cityId: Int = 0
    private val PICK_IMAGE_REQUEST_CODE: Int = 100
    private val READ_EXTERNAL_STORAGE_REQUEST_CODE: Int = 101
    var imageFile: File? = null
    var selectedBirthDate: String? = null
    public var loading: LoadingDialog? = null
    private var sex = true


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        viewModel = ViewModelProviders.of(this, RegisterFragmentFactory(this)).get(RegisterFragmentViewModel::class.java)
        binding.viewModel = viewModel

        binding.birthDate.isClickable = true
        binding.birthDate.isFocusableInTouchMode = false
        binding.birthDate.setOnClickListener {
            showDateDialog()
        }
        loading = LoadingDialog.newInstance(resources.getString(R.string.loading))

        getProvinceList()
        binding.registerBtn.typeface = FontHelper.instance.getPersianTextTypeface()
        binding.registerBtn.setOnClickListener {
            if (isValidForm()) {
                getActivationCode()
            }
        }
        binding.menOpt.typeface = FontHelper.instance.getPersianTextTypeface()
        binding.womenOpt.typeface = FontHelper.instance.getPersianTextTypeface()
        binding.segmentedView.setOnCheckedChangeListener { p0, p1 ->
            sex = p1 == R.id.menOpt
        }


        viewModel.onProvinceSelectedListener = object : RegisterFragmentViewModel.onProvinceSelected {
            override fun onSelected(provinceId: Long) {
                getCityOfProvince(provinceId)
            }
        }

        viewModel.onCitySelectedListener = object : RegisterFragmentViewModel.onCitySelected {
            override fun onSelected(cityId: Long) {
                this@RegisterFragment.cityId = cityId.toInt()
            }
        }


        binding.cameraBtn.setOnClickListener {
            pickImage()
        }

        return binding.root
    }

    private fun doRegistar() {
        val request = RegisterRequest(binding.mobile.text.toString(), "", binding.name.text.toString(), binding.familyName.text.toString(), sex, this.selectedBirthDate!!, cityId, binding.email.text.toString(), binding.aboutMe.text.toString(), binding.aboutSpouse.text.toString(), imageFile)
        (activity as LoginRegisterActivity).showCodeFragment(registerRequest = request)
    }


    fun getActivationCode() {
        callRest = WebService().getActivationCode(binding.mobile.text.toString())
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
                            loading!!.dismiss()
                            if (result.isSuccess) { // Go To Login
                                doRegistar()
                            }
                        },
                        { error ->
                            try {
                                loading!!.dismiss()

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

    private fun isValidForm(): Boolean {
        var valid = true
        var errorText = "";
        if (binding.mobile.text.toString().isBlank()) {
            valid = false
            errorText += resources.getString(R.string.pleaseFillMobile) + "\n"
            if (android.util.Patterns.PHONE.matcher(binding.mobile.text.toString()).matches()) {
                valid = false
                errorText += resources.getString(R.string.pleaseFillValidMobile) + "\n"
            }
        }

        if (binding.name.text.toString().isBlank()) {
            valid = false
            errorText += resources.getString(R.string.pleaseFillValidMobile) + "\n"

        }

        if (binding.familyName.text.toString().isBlank()) {
            valid = false
            errorText += resources.getString(R.string.pleaseFillLastName) + "\n"

        }

        if (selectedBirthDate == null) {
            valid = false
            errorText += resources.getString(R.string.pleaseFillBirthDate) + "\n"
        }

        if (imageFile == null) {
            valid = false
            errorText += resources.getString(R.string.pleaseFillProfileAvatar) + "\n"
        }
        if (!valid) {
            Toaster.toast(this.activity!!, errorText, Toast.LENGTH_LONG)
        }

        return valid
    }

    private fun showDateDialog() {
        val now = PersianCalendar()
        val dpd = DatePickerDialog.newInstance(
                this,
                now.persianYear,
                now.persianMonth,
                now.persianDay
        )
        val dates = arrayOfNulls<PersianCalendar>(54)

        for (i in -50..3) {
            val date = PersianCalendar()
            date.add(PersianCalendar.YEAR, i)
            dates[i + 50] = date
        }
        dpd.selectableDays = dates
        dpd.show(activity!!.fragmentManager, "Datepickerdialog")
    }

    override fun onDateChanged() {

    }


    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        this.year = year
        this.month = monthOfYear + 1
        this.day = dayOfMonth
        val calendarTool = CalendarTool()
        calendarTool.setIranianDate(year, monthOfYear, dayOfMonth)
        this.selectedBirthDate = calendarTool.gregorianYear.toString() + "-" + calendarTool.gregorianMonth.toString() + "-" + calendarTool.gregorianDay
        binding.birthDate.setText(this.year.toString() + "/" + this.month.toString() + "/" + this.day.toString())
    }

    fun getProvinceList() {
        callRest = WebService().getProvince()
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
                                binding.province.adapter = ProvinceCitySpinnerAdapter(result.listItems!!)
                            }
                        },
                        { error ->
                        }
                )
    }

    fun getCityOfProvince(provinceId: Long) {
        callRest = WebService().getCity(provinceId.toInt()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
            if (!loading!!.isAdded)
                loading!!.show(childFragmentManager, "dialog")
        }.doOnTerminate {
            loading!!.dismiss()
        }.subscribe(
                { result ->
                    if (result.isSuccess) {
                        binding.city.adapter = ProvinceCitySpinnerAdapter(result.listItems!!)
                    }

                },
                { error ->
                }
        )
    }

    override fun onStop() {
        super.onStop()
        if (::callRest.isInitialized && !callRest.isDisposed) {
            callRest.dispose()
        }
    }

    private fun pickImage() {
        if (ActivityCompat.checkSelfPermission(this.activity!!, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                    this.activity!!,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }
            val uri = data?.data
            if (uri != null) {
                imageFile = uriToImageFile(uri)
                ImageLoaderHelper.displayCircleImage(this.activity!!, binding.profileAvatar, uri)
//                binding.profileAvatar.setImageURI(uri)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // pick image after request permission success
                    pickImage()
                }
            }
        }
    }

    private fun uriToImageFile(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity!!.contentResolver.query(uri, filePathColumn, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val filePath = cursor.getString(columnIndex)
                cursor.close()
                return File(filePath)
            }
            cursor.close()
        }
        return null
    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)
    }
}