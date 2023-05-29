package com.example.weatherforecast.ui

import com.example.weatherforecast.domain.model.City
import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.ui.Model.CityUI
import com.example.weatherforecast.ui.Model.WeatherUI

internal fun City.toUI(): CityUI = CityUI(cityName = cityName)

internal fun List<Weather>.toUI(): List<WeatherUI> = map {
    it.toUI()
}

internal fun Weather.toUI(): WeatherUI = WeatherUI(
    dtTxt = dtTxt, temp = temperature, pressure = pressure, icon = iconURL
)