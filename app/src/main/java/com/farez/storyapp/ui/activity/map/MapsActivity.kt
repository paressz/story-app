package com.farez.storyapp.ui.activity.map

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.farez.storyapp.R
import com.farez.storyapp.api.ApiConfig
import com.farez.storyapp.data.repository.StoryRepository
import com.farez.storyapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.farez.storyapp.data.remote.Result
import com.farez.storyapp.databinding.DialogLayoutBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(),GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapViewModel: MapViewModel
    private lateinit var mapVMFactory: MapVMFactory
    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        token = intent.getStringExtra("token").toString()
        setupViewModel()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.apply{
            isZoomControlsEnabled = true
            isCompassEnabled = true
        }
        mMap.setOnMarkerClickListener(this)
        setupStory()
        supportActionBar?.hide()
        getMyLocation()
    }
    // SETUP VIEWS
    fun setupViewModel() {
        mapVMFactory = MapVMFactory(
            StoryRepository.getInstance(ApiConfig.getApiService())
        )
        mapViewModel = ViewModelProvider(this, mapVMFactory)[MapViewModel::class.java]
    }

    fun setupStory() {
        mapViewModel.getMapStory(token).observe(this) {
            mMap.clear()
            when (it) {
                is Result.Success -> {
                    val stories = it.data
                    stories.forEach { story ->
                        val latLng = LatLng(story.lat, story.lon)
                        val pin = mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(story.name)
                                .snippet(story.description)
                        )
                        pin?.tag = story.photoUrl
                        mMap.setOnMarkerClickListener(this)
                    }
                }

                is Result.Error -> {
                    Toast.makeText(this, "error saat memuat data", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {

                }
            }
        }
    }

    fun setupDialog(name : String, desc : String, photoUrl : String) {
        val dialogBinding : DialogLayoutBinding = DialogLayoutBinding.inflate(layoutInflater)
        val dialog = Dialog(this@MapsActivity)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialogBinding.apply {
            button.setOnClickListener {
                dialog.dismiss()
            }
            tvDetailName.text = name
            tvDetailDescription.text = desc
            Glide
                .with(this@MapsActivity)
                .load(photoUrl)
                .into(ivDetailPhoto)
        }
        dialog.show()
    }

    override fun onMarkerClick(marker : Marker): Boolean {
        setupDialog(marker.title.toString(), marker.snippet.toString(), marker.tag.toString())
        return false
    }

    // PERMISSION FUNCTIONS
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}