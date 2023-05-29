package com.example.weatherforecast

import android.app.Application
import androidx.room.Room
import com.example.weatherforecast.data.WeatherRepositoryImpl
import com.example.weatherforecast.data.local.WeatherDataBase
import com.example.weatherforecast.data.network.WeatherAPI
import com.example.weatherforecast.domain.WeatherRepository
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        initApi()
        initDatabase()
        initRepository()
        super.onCreate()
    }

    companion object {
        lateinit var weatherAPI: WeatherAPI
        lateinit var weatherDatabase: WeatherDataBase
        lateinit var repository: WeatherRepository
    }
    private fun initRepository() {
        repository = WeatherRepositoryImpl(weatherAPI, weatherDatabase)
    }

    private fun initApi() {
        weatherAPI = WeatherAPI.createAPI()
    }

    private fun initDatabase() {
        weatherDatabase = WeatherDataBase.getDatabase(this)
    }

}