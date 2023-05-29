package com.example.weatherforecast.data

import android.util.Log
import com.example.weatherforecast.Constants
import com.example.weatherforecast.Constants.API_KEY
import com.example.weatherforecast.Constants.API_LANG
import com.example.weatherforecast.Constants.API_UNITS
import com.example.weatherforecast.data.local.CitySW
import com.example.weatherforecast.data.local.PositionSW
import com.example.weatherforecast.data.local.WeatherDataBase
import com.example.weatherforecast.data.local.WeatherSW
import com.example.weatherforecast.data.network.WeatherAPI
import com.example.weatherforecast.domain.WeatherData
import com.example.weatherforecast.domain.WeatherRepository
import com.example.weatherforecast.domain.model.Position

sealed interface WhereGetWeatherData {
    data class FromNetWork(val weather: WeatherData) : WhereGetWeatherData
    data class FromDataBase(val weather: WeatherData): WhereGetWeatherData
}

class WeatherRepositoryImpl(
    private val weatherAPI: WeatherAPI,
    private val dataBase: WeatherDataBase
) : WeatherRepository {

    override suspend fun loadWeather(lat: String, lon: String): WhereGetWeatherData {
        try {
            val weather1 = weatherAPI.getForecast(lat, lon, API_KEY, API_UNITS, API_LANG)
            updateWeatherDB(weather1.list.toSW(), weather1.city.toSW(), Position(lat, lon).toSW())
            return WhereGetWeatherData.FromNetWork(weather1.toDomain())
        } catch (e: java.lang.Exception) {
            Log.e("aaa", "Exception ${e.message}")
            val weather = WeatherData(
                getWeatherDB().toDomain(),
                getCityDB().toDomain(),
                getPositionDB().toDomain()
            )
            return WhereGetWeatherData.FromDataBase(weather)
        }
    }
    private suspend fun getPositionDB() = dataBase.weatherDao().getPosition()

    private suspend fun getCityDB() = dataBase.weatherDao().getCity()

    private suspend fun getWeatherDB() = dataBase.weatherDao().getWeather()

    private suspend fun updateWeatherDB(
        weatherList: List<WeatherSW>,
        cityName: CitySW,
        position: PositionSW
    ) =
        dataBase.weatherDao().updateWeather(weatherList, cityName, position)
}