package com.relgram.app.app.view.fragments

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.relgram.app.app.R
import com.relgram.app.app.databinding.FragmentGalleryBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.library.Toaster
import com.relgram.app.app.models.BaseResponse
import com.relgram.app.app.view.dialog.LoadingDialog
import com.relgram.app.app.view.factories.UserFragmentFactory
import com.relgram.app.app.view_models.GalleryFragmentViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.File

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var viewModel: GalleryFragmentViewModel
    private lateinit var callRest: Disposable
    private val PICK_IMAGE_REQUEST_CODE: Int = 100
    private val PICK_AVATAR_REQUEST_CODE: Int = 101
    private val READ_EXTERNAL_STORAGE_REQUEST_CODE: Int = 102
    private var imageFile: File? = null
    public var loading: LoadingDialog? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)
        viewModel = ViewModelProviders.of(this, UserFragmentFactory(this)).get(GalleryFragmentViewModel::class.java)
        binding.viewModel = viewModel
        loading = LoadingDialog.newInstance(resources.getString(R.string.loading))
        getMyGallery()

        binding.addImage.typeface = FontHelper.instance.getPersianTextTypeface()

        binding.addImage.setOnClickListener {
            pickImage(PICK_IMAGE_REQUEST_CODE)
        }

        binding.profileAvatar.setOnClickListener {
            pickImage(PICK_AVATAR_REQUEST_CODE)
        }

        binding.deleteImage1.setOnClickListener { view ->
            if (view.tag != 0) {
                deleteImage(view.tag as Long)
            }

        }

        binding.deleteImage2.setOnClickListener { view ->
            if (view.tag != 0) {
                deleteImage(view.tag as Long)
            }

        }

        binding.deleteImage3.setOnClickListener { view ->
            if (view.tag != 0) {
                deleteImage(view.tag as Long)
            }

        }

        binding.deleteImage4.setOnClickListener { view ->
            if (view.tag != 0) {
                deleteImage(view.tag as Long)
            }

        }

        binding.deleteImage5.setOnClickListener { view ->
            if (view.tag != 0) {
                deleteImage(view.tag as Long)
            }

        }


        return binding.root
    }

    private fun pickImage(request: Int) {
        if (ActivityCompat.checkSelfPermission(this.activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            startActivityForResult(intent, request)
        } else {
            ActivityCompat.requestPermissions(
                    this.activity!!,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    request
            )
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // pick image after request permission success
            pickImage(requestCode)
        }
    }

    private fun getMyGallery() {
        callRest = WebService().getMyGallery().subscribeOn(Schedulers.io())
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
                            if (result.isSuccess && result.item != null) {
                                viewModel.bind(result.item!!)
                                binding.viewModel = viewModel
                            }
                        },
                        { error ->
                            print(error.message)
                        }
                )

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
                startUploadingFile()
            }
        } else if (requestCode == PICK_AVATAR_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }
            val uri = data?.data
            if (uri != null) {
                imageFile = uriToImageFile(uri)
                uploadAvatar()
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

    private fun startUploadingFile() {
        callRest = WebService().uploadImage(this.imageFile!!).subscribeOn(Schedulers.io())
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
                                Toaster.toast(this.context!!, R.string.uploadSuccesfully)
                                imageFile = null
                                getMyGallery()
                            }
                        },
                        { error ->
                            try {
                                val errorResult = Gson().fromJson((error as HttpException).response().errorBody()!!.string(), BaseResponse::class.java)
                                if (errorResult.errorCode == 106) {
                                    Toaster.toast(this.context!!, R.string.moreThan5Image)
                                } else {
                                    Toaster.toast(this.context!!, R.string.uploadNotSuccesfully)
                                }
                            } catch (e: Exception) {
                                Toaster.toast(this.context!!, R.string.uploadNotSuccesfully)
                            }
                        }
                )

    }

    private fun uploadAvatar() {
        callRest = WebService().uploadAvatar(this.imageFile!!).subscribeOn(Schedulers.io())
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
                                Toaster.toast(this.context!!, R.string.uploadSuccesfully)
                                imageFile = null
                                getMyGallery()
                            }
                        },
                        { error ->
                            try {
                                val errorResult = Gson().fromJson((error as HttpException).response().errorBody()!!.string(), BaseResponse::class.java)
                                if (errorResult.errorCode == 106) {
                                    Toaster.toast(this.context!!, R.string.moreThan5Image)
                                } else {
                                    Toaster.toast(this.context!!, R.string.uploadNotSuccesfully)
                                }
                            } catch (e: Exception) {
                                Toaster.toast(this.context!!, R.string.uploadNotSuccesfully)
                            }
                        }
                )
    }

    private fun deleteImage(id: Long) {
//        TODO : SHowDialog
        val rest = WebService().deleteGallery(id).subscribeOn(Schedulers.io())
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
                                getMyGallery()
                            }
                        },
                        { error ->
                            print(error.message)
                        }
                )
    }


}