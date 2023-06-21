package com.farez.storyapp.ui.activity.Upload

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.farez.storyapp.api.ApiConfig
import com.farez.storyapp.data.remote.Result
import com.farez.storyapp.data.repository.StoryRepository
import com.farez.storyapp.databinding.ActivityUploadBinding
import com.farez.storyapp.reduceFileImage
import com.farez.storyapp.rotateBitmap
import com.farez.storyapp.ui.activity.Story.StoryActivity
import com.farez.storyapp.ui.activity.Upload.CameraXActivity.Companion.CAMERA_X_RESULT
import com.farez.storyapp.uriToFile
import java.io.File
import java.io.FileOutputStream

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var token : String
    private var file : File? = null
    private lateinit var uploadViewModel : UploadViewModel
    private lateinit var uploadVMFactory: UploadVMFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        supportActionBar?.hide()
        token = intent.getStringExtra("token").toString()
        binding.apply {
            cameraButton.setOnClickListener {
                val intent = Intent(this@UploadActivity, CameraXActivity::class.java)
                launcherIntentCameraX.launch(intent)
            }
            galleryButton.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = "image/*"
                val chooser = Intent.createChooser(intent, "Choose a Picture")
                launcherIntentGallery.launch(chooser)
            }
            uploadButton.setOnClickListener {
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        if (binding.textInputLayout.editText?.text.toString().isEmpty()) {
            Toast.makeText(this@UploadActivity, "deksripsi harus diisi", Toast.LENGTH_SHORT).show()
        }
        if (file != null) {
            val compressFile = reduceFileImage(file as File)
            val desc = binding.textInputLayout.editText?.text.toString()
            uploadViewModel.newStory(token, compressFile, desc).observe(this) {
                when (it) {
                    is Result.Loading -> {
                        binding.progressBar3.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.progressBar3.visibility = View.GONE
                        Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        binding.progressBar3.visibility = View.GONE
                        val intent = Intent(this, StoryActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

        }
    }
    private fun setupViewModel() {
        uploadVMFactory = UploadVMFactory(StoryRepository(ApiConfig.getApiService()))
        uploadViewModel = ViewModelProvider(this, uploadVMFactory)[UploadViewModel::class.java]
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@UploadActivity)
            file = myFile
            binding.imageView.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(myFile))
            file = myFile
            binding.imageView.setImageBitmap(result)
        }
    }
}