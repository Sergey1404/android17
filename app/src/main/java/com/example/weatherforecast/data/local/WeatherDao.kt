package com.example.weatherforecast.data.local

import androidx.room.*

@Dao
interface WeatherDao {
    @Insert
    suspend fun saveWeather(weather: List<WeatherSW>)

    @Query(value = "Select * from WeatherSW")
    suspend fun getAllWeather() : List<WeatherSW>

    @Query("DELETE FROM WeatherSW")
    suspend fun clearWeather()

    @Query("SELECT * FROM WeatherSW")
    suspend fun getWeather(): List<WeatherSW>

    @Transaction
    suspend fun updateWeather(weather: List<WeatherSW>, city: CitySW, position: PositionSW) {
        clearAllTables()
        saveWeather(weather)
        putCity(city)
        putPosition(position)
    }
    @Transaction
    suspend fun WeatherDao.clearAllTables() {
        clearWeather()
        clearCity()
        clearPosition()
    }

    @Insert
    suspend fun putCity(citySW: CitySW)

    @Query("SELECT * FROM CitySW")
    suspend fun getCity(): CitySW

    @Query("DELETE FROM CitySW")
    suspend fun clearCity()

    @Insert
    suspend fun putPosition(positionSW: PositionSW)

    @Query("SELECT * FROM PositionSW")
    suspend fun getPosition(): PositionSW

    @Query("DELETE FROM PositionSW")
    suspend fun clearPosition()
}