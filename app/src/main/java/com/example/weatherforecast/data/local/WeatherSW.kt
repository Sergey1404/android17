package com.example.weatherforecast.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherSW(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val temperature: Double,
    val pressure: Int,
    val dtTxt: String,
    val iconURL: String
)
@Entity
class CitySW(
    @PrimaryKey
    @ColumnInfo(name = "city_name")
    val cityName: String
)
@Entity
class PositionSW(
    @PrimaryKey
    @ColumnInfo(name = "position")
    val lat: String,
    val lon: String
)
