package com.relgram.app.app.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mojtaba.materialdatetimepicker.date.DatePickerDialog
import com.mojtaba.materialdatetimepicker.utils.PersianCalendar
import com.relgram.app.app.R
import com.relgram.app.app.databinding.FragmentEditBaseInfoBinding
import com.relgram.app.app.library.CalendarTool
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.library.Toaster
import com.relgram.app.app.models.EditProfileRequest
import com.relgram.app.app.models.ProvinceCityResponse
import com.relgram.app.app.view.adapters.ProvinceCitySpinnerAdapter
import com.relgram.app.app.view.dialog.LoadingDialog
import com.relgram.app.app.view.factories.UserFragmentFactory
import com.relgram.app.app.view_models.RegisterFragmentViewModel
import com.relgram.app.app.view_models.UserFragmentViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EditBaseInfoFragment : Fragment(), DatePickerDialog.OnDateChangedListener, DatePickerDialog.OnDateSetListener {

    private lateinit var callRest: Disposable
    lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: FragmentEditBaseInfoBinding
    private lateinit var viewModel: UserFragmentViewModel
    private var cityId: Int = 0
    private var provinceId: Int = 0
    private var year: Int? = null
    private var month: Int? = null
    private var day: Int? = null
    private var persianYearSelected: Int? = null
    private var persianMonthSelected: Int? = null
    private var persianDaySelected: Int? = null
    private var sex = true
    lateinit var selectedBirthDate: String
    public var loading: LoadingDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_base_info, container, false)
        viewModel = ViewModelProviders.of(this, UserFragmentFactory(this)).get(UserFragmentViewModel::class.java)
        loading = LoadingDialog.newInstance(resources.getString(R.string.loading))

        getMyInfo()

        binding.birthDate.isClickable = true
        binding.birthDate.isFocusableInTouchMode = false
        binding.birthDate.setOnClickListener {
            showDateDialog()
        }

        binding.registerBtn.typeface = FontHelper.instance.getPersianTextTypeface()
        binding.registerBtn.setOnClickListener {
            edit()
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
                this@EditBaseInfoFragment.cityId = cityId.toInt()
            }
        }

        return binding.root
    }


    fun getMyInfo() {
        callRest = WebService().getMyInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (!loading!!.isAdded)
                        loading!!.show(childFragmentManager, "dialog")

                }
                .doOnTerminate {
                    loading!!.dismiss()
                    getProvinceList()

                }
                .subscribe(
                        { result ->
                            if (result.isSuccess && result.item != null) {
                                cityId = result.item!!.cityId
                                provinceId = result.item!!.provinceId
                                sex = result.item!!.sex
                                selectedBirthDate = result.item!!.birthDate!!
                                setPersianDateToDialog(result.item!!.birthDate)
                                viewModel.bind(result.item!!)
                                binding.viewModel = viewModel
                            }
                        },
                        { error -> print(error.message) }
                )

    }

    private fun setPersianDateToDialog(bDate: String?) {
        if (bDate == null) return
        val now = PersianCalendar()
        val spDate = bDate.split("-")
        now.set(spDate[0].toInt(), spDate[1].toInt(), spDate[2].toInt())
        persianYearSelected = now.persianYear
        persianMonthSelected = now.persianMonth
        persianDaySelected = now.persianDay

    }


    fun getProvinceList() {
        callRest = WebService().getProvince()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                }
                .doOnTerminate {
                }
                .subscribe(
                        { result ->
                            if (result.isSuccess) {
                                binding.province.adapter = ProvinceCitySpinnerAdapter(result.listItems!!)
                                binding.province.setSelection(getProvincePosition(result.listItems!!))

                            }
                        },
                        { error ->
                        }
                )
    }

    fun getCityOfProvince(provinceId: Long) {
        callRest = WebService().getCity(provinceId.toInt()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
        }.doOnTerminate {
        }.subscribe(
                { result ->
                    if (result.isSuccess) {
                        binding.city.adapter = ProvinceCitySpinnerAdapter(result.listItems!!)
                        binding.city.setSelection(getCityPosition(result.listItems!!))
                    }
                },
                { error ->
                }
        )
    }


    fun edit() {
        var firstName = binding.name.text.toString()
        var lastName = binding.familyName.text.toString()
        var email = binding.email.text.toString()
        var aboutMe = binding.aboutMe.text.toString()
        var aboutSpouse = binding.aboutSpouse.text.toString()
        var birthDate = binding.birthDate.text.toString().split("/")
        if (birthDate.size == 3) {
            val calendarTool = CalendarTool()
            calendarTool.setIranianDate(birthDate[0].toInt(), birthDate[1].toInt(), birthDate[2].toInt())
            this.selectedBirthDate = calendarTool.gregorianYear.toString() + "-" + calendarTool.gregorianMonth.toString() + "-" + calendarTool.gregorianDay
        } else {
            Toaster.toast(activity!!, R.string.birthDateInvalid)
            return
        }
        callRest = WebService().editUser(EditProfileRequest(firstName, lastName, cityId, email, selectedBirthDate, aboutMe, aboutSpouse, sex))
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (!loading!!.isAdded)
                        loading!!.show(childFragmentManager, "dialog")
                }
                .doOnTerminate {
                }
                .subscribe(
                        { result ->
                            if (result.isSuccess) {
                                Toaster.toast(this!!.activity!!, R.string.sendInfoSucessfull)
                                getMyInfo()
                            }

                        },
                        { error ->
                        }
                )
    }

    fun getProvincePosition(provinceList: List<ProvinceCityResponse>): Int {
        var index = 0
        for (item in provinceList) {
            if (item.id == provinceId) {
                return index
            }
            index++
        }
        return 0
    }


    fun getCityPosition(cityList: List<ProvinceCityResponse>): Int {
        var index = 0
        for (item in cityList) {
            if (item.id == cityId) {
                return index
            }
            index++
        }
        return 0
    }

    private fun showDateDialog() {
        val now = PersianCalendar()
        val dpd: DatePickerDialog
        if (persianYearSelected != null && persianDaySelected != null && persianMonthSelected != null) {
            dpd = DatePickerDialog.newInstance(
                    this,
                    persianYearSelected!!,
                    persianMonthSelected!! - 1,
                    persianDaySelected!!
            )
        } else {
            dpd = DatePickerDialog.newInstance(
                    this,
                    now.persianYear,
                    now.persianMonth,
                    now.persianDay
            )
        }
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
        persianYearSelected = this.year
        persianMonthSelected = this.month
        persianDaySelected = this.day
        val calendarTool = CalendarTool()
        calendarTool.setIranianDate(year, monthOfYear, dayOfMonth)
        this.selectedBirthDate = calendarTool.gregorianYear.toString() + "-" + calendarTool.gregorianMonth.toString() + "-" + calendarTool.gregorianDay
        binding.birthDate.setText(this.year.toString() + "/" + this.month.toString() + "/" + this.day.toString())
    }
}