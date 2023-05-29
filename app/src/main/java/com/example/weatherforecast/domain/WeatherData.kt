package com.example.weatherforecast.domain

import com.example.weatherforecast.domain.model.City
import com.example.weatherforecast.domain.model.Position
import com.example.weatherforecast.domain.model.Weather

data class WeatherData(
    val weatherList: List<Weather>,
    val city: City,
    val position: Position
)
