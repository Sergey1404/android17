package com.example.weatherforecast

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.ui.Model.WeatherUI
import java.lang.IllegalArgumentException
private const val TYPE_COLD = 0
private const val TYPE_WARM = 1

class WeatherAdapter(
    private val onLongClick:(WeatherUI) -> Unit
): ListAdapter<WeatherUI, RecyclerView.ViewHolder>(WeatherDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_COLD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather_cold_holder, parent, false)
                WeatherColdHolder(view, onLongClick)
            }
            TYPE_WARM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather_warm_holder, parent, false)
                WeatherWarmHolder(view, onLongClick)
            }
            else ->{
                throw IllegalArgumentException("Missing type of holder")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_COLD -> {
                (holder as WeatherColdHolder).bind(getItem(position))
            }
            TYPE_WARM -> {
                (holder as WeatherWarmHolder).bind(getItem(position))
            }
            else -> throw IllegalArgumentException("Invalid temperature")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).temp > -2) {
            TYPE_WARM
        } else {
            TYPE_COLD
        }
    }
}

private fun getImage(weather: WeatherUI) =
    "https://openweathermap.org/img/wn/${weather.icon}@2x.png"

class WeatherColdHolder(private val view: View, private val onLongClick: (weather: WeatherUI) -> Unit) :
    RecyclerView.ViewHolder(view.rootView) {
    fun bind(weather: WeatherUI) {
        val weatherTimeDate = weather.dtTxt.split(" ")
        view.findViewById<TextView>(R.id.tvTemperature).text =
            view.rootView.context.getString(R.string.temperature, weather.temp.toString())
        view.findViewById<TextView>(R.id.tvTime).text = weatherTimeDate.first()
        view.findViewById<TextView>(R.id.tvDate).text = weatherTimeDate.last()
        view.findViewById<TextView>(R.id.tvPressure).text = weather.pressure.toString()
        Glide
            .with(view.rootView)
            .load(getImage(weather))
            .into(view.findViewById(R.id.ivIcon))

        view.setOnLongClickListener {
            Log.d("aaa", "ColdLongClick")

            onLongClick(weather)
            true
        }
    }
}

class WeatherWarmHolder(private val view: View, private val onLongClick: (weather: WeatherUI) -> Unit) :
    RecyclerView.ViewHolder(view.rootView) {
    fun bind(weather: WeatherUI) {
        val weatherTimeDate = weather.dtTxt.split(" ")
        view.findViewById<TextView>(R.id.tvTemperature).text =
            view.rootView.context.getString(R.string.temperature, weather.temp.toString())
        view.findViewById<TextView>(R.id.tvTime).text = weatherTimeDate.first()
        view.findViewById<TextView>(R.id.tvDate).text = weatherTimeDate.last()
        view.findViewById<TextView>(R.id.tvPressure).text = weather.pressure.toString()
        Glide
            .with(view.rootView)
            .load(getImage(weather))
            .into(view.findViewById(R.id.ivIcon))

        view.setOnClickListener {
            Log.d("aaa", "WarmLongClick")

            onLongClick(weather)
            true
        }
    }
}

class WeatherDiffCallback : DiffUtil.ItemCallback<WeatherUI>() {
    override fun areItemsTheSame(
        oldItem: WeatherUI,
        newItem: WeatherUI
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: WeatherUI,
        newItem: WeatherUI
    ): Boolean = oldItem == newItem
}