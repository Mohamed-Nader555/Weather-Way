package com.mohamednader.weatherway.Home.View.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.weatherway.Model.Pojo.Hourly
import com.mohamednader.weatherway.Utilities.Constants
import com.mohamednader.weatherway.Utilities.getWeatherImageDrawable
import com.mohamednader.weatherway.databinding.ItemHourlyBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HourlyAdapter(private val context: Context) :
    ListAdapter<Hourly, HourlyViewHolder>(HourlyDiffUtil()) {

    private lateinit var binding: ItemHourlyBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        binding = ItemHourlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val hourly : Hourly = getItem(position)

        val img = hourly.weather[0].icon
        val temp = "${hourly.temp.toFloat().roundToInt()}${Constants.tempUnitForAll}"
        val dateFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        val date = Date(hourly.dt.toLong() * 1000)

        holder.binding.hourlyImage.setImageResource(getWeatherImageDrawable(img))
        holder.binding.hourlyWeatherTemp.text = temp
        holder.binding.hourlyTime.text = dateFormat.format(date).toString()
    }

}


class HourlyViewHolder(var binding: ItemHourlyBinding) :
    RecyclerView.ViewHolder(binding.root)

class HourlyDiffUtil : DiffUtil.ItemCallback<Hourly>() {
    override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem == newItem
    }

}