package com.mohamednader.weatherway.Home.View.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.weatherway.Model.Pojo.Daily
import com.mohamednader.weatherway.Utilities.getWeatherImageDrawable
import com.mohamednader.weatherway.databinding.ItemDailyBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DailyAdapter(private val context: Context, private val listener: OnDayClickListener) :
    ListAdapter<Daily, DailyViewHolder>(DailyDiffUtil()) {

    private lateinit var binding: ItemDailyBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        binding = ItemDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        //get current object
        val daily: Daily = getItem(position)

        //prepare data to set on Views
        val img = daily.weather[0].icon
        val temp = daily.temp.day.toFloat().roundToInt().toString()
        val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val date = Date(daily.dt.toLong() * 1000)

        //set Data on Views
        holder.binding.dailyImage.setImageResource(getWeatherImageDrawable(img))
        holder.binding.dailyTemp.text = temp
        holder.binding.dailyDay.text = dateFormat.format(date)

        //set click listener on the view
        holder.binding.root.setOnClickListener {
            listener.onDayClickListener()
        }
    }

}

class DailyViewHolder(var binding: ItemDailyBinding) : RecyclerView.ViewHolder(binding.root)

class DailyDiffUtil : DiffUtil.ItemCallback<Daily>() {
    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem == newItem
    }

}