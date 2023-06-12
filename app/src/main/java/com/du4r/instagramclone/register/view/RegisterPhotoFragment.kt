package com.du4r.instagramclone.register.view

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.du4r.instagramclone.R
import com.du4r.instagramclone.common.base.DependencyInjector
import com.du4r.instagramclone.common.view.CropperImageFragment
import com.du4r.instagramclone.common.view.CustomDialog
import com.du4r.instagramclone.databinding.FragmentRegisterPhotoBinding
import com.du4r.instagramclone.register.RegisterPhoto
import com.du4r.instagramclone.register.presentation.RegisterPhotoPresenter

class RegisterPhotoFragment: Fragment(R.layout.fragment_register_photo), RegisterPhoto.View {
    private var binding: FragmentRegisterPhotoBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null
    override lateinit var presenter: RegisterPhoto.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //escuta o setFragmentResult
        // recebendo a nova uri croppada e
        setFragmentResultListener("cropKey"){ requestKey, bundle ->  
            val uri = bundle.getParcelable<Uri>(CropperImageFragment.KEY_URI)
            onCropImageResult(uri!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegisterPhotoBinding.bind(view)

        val repository = DependencyInjector.registerEmailRepository()
        presenter = RegisterPhotoPresenter(this,repository)

        binding?.let {
            with(it){
                registerBtnSkip.setOnClickListener {
                    fragmentAttachListener?.goToMainScreen()
                }
                registerBtnNext.isEnabled = true

                registerBtnNext.setOnClickListener{
                    openDialog()
                }

            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentAttachListener){
            fragmentAttachListener = context
        }
    }

    // recebe a uri da image croppada e insere na registerImgProfile
    private fun onCropImageResult(uri: Uri){
        if(uri != null){
            val bitmap = if(Build.VERSION.SDK_INT >= 28){
                val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else{
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver,uri)
            }
            binding?.registerImgProfile?.setImageBitmap(bitmap)
            presenter.updateUser(uri)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        presenter.onDestroy()
    }

    private fun openDialog(){
        val customDialog = CustomDialog(requireContext())

        customDialog.setTitle(R.string.app_name)

        customDialog.addButton(R.string.photo,R.string.gallery){
            when(it.id){
                R.string.photo -> fragmentAttachListener?.goToCameraScreen()
                R.string.gallery -> fragmentAttachListener?.goToGalleryScreen()
            }
        }
        customDialog.show()
    }

    override fun showProgress(enabled: Boolean) {
        binding?.registerBtnNext?.showProgress(enabled)
    }

    override fun onUpdateFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onUpdateSuccess() {
        fragmentAttachListener?.goToMainScreen()
    }


}