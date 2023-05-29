package com.example.weatherforecast.data.network

import com.example.weatherforecast.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//Todo
interface WeatherAPI {
    companion object {
        fun createAPI(): WeatherAPI {
            val interceptorBody = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptorBody).build()
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofitBuilder.create(WeatherAPI::class.java)
        }
    }

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") key: String,
        @Query("units") units: String,
        @Query("lang") language: String
    ): WeatherNW
}