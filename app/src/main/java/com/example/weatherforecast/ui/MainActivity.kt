package com.example.weatherforecast.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.*
import com.example.weatherforecast.App.Companion.repository
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.ui.Model.WeatherUI
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private val weatherAdapter = WeatherAdapter(
        onLongClick = {
            Log.d("aaa", "$it")
            shareWeather(it)
        }
    )
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(repository) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.M)
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getLocation()
        recycleViewInit()
        observeModel()

        showSnackBar(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        }

        fusedLocationClient.requestLocationUpdates(
            LocationRequest.create(),
            object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    Log.d("aaa", p0.toString())
                    viewModel.loadWeather(p0.lastLocation.latitude.toString(), p0.lastLocation.longitude.toString())
                }
            },
            Looper.getMainLooper()
        )
    }


    private fun showSnackBar(view: View) {
        viewModel.isFromDataBase.observe(this) {
            it.getContentIfNotHandled()?.let {
                val mySnackbar = Snackbar.make(view, "Данные загружены", Snackbar.LENGTH_LONG)
                mySnackbar.show()
            }
        }
    }

    private fun recycleViewInit() {
        binding.rvWeather.adapter = weatherAdapter
        binding.rvWeather.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun observeModel() {
        observeWeatherModel()
        observeErrorEvent()
    }

    private fun observeErrorEvent() {
        viewModel.errorNetwork.observe(this) {
            it.getContentIfNotHandled()?.let {
                Snackbar
                    .make(binding.root, "Server is not responding", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun observeWeatherModel() {
        viewModel.weatherList.observe(this) {
            weatherAdapter.submitList(it)
        }
    }

    private fun shareWeather(weather: WeatherUI) {
        val sendIntent = Intent().apply {
            val weatherTimeDate = weather.dtTxt.split(" ")

            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "${Constants.API_CITY} ${weather.dtTxt} ${weather.temp}"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Send text")
        startActivity(shareIntent)
    }
}