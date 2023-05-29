package com.example.weatherforecast.domain

import com.example.weatherforecast.data.WhereGetWeatherData

interface WeatherRepository {
    suspend fun loadWeather(lat: String, lon: String): WhereGetWeatherData
}