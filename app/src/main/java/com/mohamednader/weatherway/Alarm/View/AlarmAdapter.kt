package com.mohamednader.weatherway.Alarm.View

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.weatherway.Model.AlarmItem
import com.mohamednader.weatherway.databinding.ItemAlarmBinding


class AlarmAdapter(
    private val context: Context, private val listener: OnAlarmClickListener
) : ListAdapter<AlarmItem, AlarmViewHolder>(AlarmDiffUtil()) {

    private lateinit var binding: ItemAlarmBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {

        val alarm: AlarmItem = getItem(position)

        binding.alarmAddress.text = alarm.address
        binding.alarmTime.text = alarm.time

        binding.deleteImgBtn.setOnClickListener {
            listener.onClickDeleteAlarm(alarm)
        }

    }

}

class AlarmViewHolder(var binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root)

class AlarmDiffUtil : DiffUtil.ItemCallback<AlarmItem>() {
    override fun areItemsTheSame(oldItem: AlarmItem, newItem: AlarmItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: AlarmItem, newItem: AlarmItem): Boolean {
        return oldItem == newItem
    }

}

