package com.mohamednader.weatherway.Alarm.View

import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.weatherway.Alarm.AlarmComponents.AlarmReceiver
import com.mohamednader.weatherway.Alarm.ViewModel.AlarmFragmentViewModel
import com.mohamednader.weatherway.Alarm.ViewModel.AlarmFragmentViewModelFactory
import com.mohamednader.weatherway.Database.ConcreteLocalSource
import com.mohamednader.weatherway.MainHome.View.FabClickListener
import com.mohamednader.weatherway.Model.AlarmItem
import com.mohamednader.weatherway.Model.Repo.Repository
import com.mohamednader.weatherway.Network.ApiClient
import com.mohamednader.weatherway.R
import com.mohamednader.weatherway.SharedPreferences.ConcreteSharedPrefsSource
import com.mohamednader.weatherway.Utilities.Constants
import com.mohamednader.weatherway.databinding.FragmentAlarmBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class AlarmFragment : Fragment(), FabClickListener, OnDateSetListener, OnTimeSetListener, OnAlarmClickListener {

    private val TAG = "AlarmFragment_INFO_TAG"

    private lateinit var binding: FragmentAlarmBinding
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    lateinit var datePickerDialog: DatePickerDialog
    lateinit var timePickerDialog: TimePickerDialog

    private val REQUEST_OVERLAY_PERMISSION = 1001
    private var alarmIdCounter = 0

    //View Model Members
    private lateinit var alarmViewModel: AlarmFragmentViewModel
    private lateinit var alarmFactory: AlarmFragmentViewModelFactory

    lateinit var alarmAdapter: AlarmAdapter
    lateinit var formattedDateTime : String

    lateinit var notif: String

    private val updateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getIntExtra(Constants.alarmIdKey, 0)
            Log.i(TAG, "onReceive: FROM FRAGMENT $id")
            alarmViewModel.deleteAlarmFromFav(id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlarmBinding.inflate(inflater, container, false)

        val intentFilter = IntentFilter("com.mohamednader.weatherway.ACTION_ALARM")
        requireContext().registerReceiver(updateReceiver, intentFilter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createNotificationChannel()
        calendar = Calendar.getInstance()
        alarmIdCounter = getSavedAlarmIdCounter()

        //ViewModel
        alarmFactory = AlarmFragmentViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(requireContext()),
                ConcreteSharedPrefsSource(requireContext())
            )
        )
        alarmViewModel =
            ViewModelProvider(this, alarmFactory).get(AlarmFragmentViewModel::class.java)

        recyclerViewConfig()

        lifecycleScope.launchWhenStarted {
            launch {

                alarmViewModel.alarmsList.collect { alarms ->

                    if (alarms.isNotEmpty()) {
                        showViews()
                        alarmAdapter.submitList(alarms)
                    } else {
                        hideViews()
                    }
                }
            }

            launch {
                alarmViewModel.notification.collect { result ->
                    when (result) {
                        Constants.notification_enable -> {
                            notif = result
                        }
                        Constants.notification_disable -> {
                            notif = result
                        }
                    }
                }
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().unregisterReceiver(updateReceiver)
    }


    private fun recyclerViewConfig() {
        binding.alarmRecyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.alarmRecyclerView.layoutManager = linearLayoutManager
        alarmAdapter = AlarmAdapter(requireContext(), this)
        binding.alarmRecyclerView.adapter = alarmAdapter
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alarms"
            val descriptionText = "Alarm Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("alarms", name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.RED
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onFabClick() {

        val calendarDate = Calendar.getInstance()
        val currentYear = calendarDate.get(Calendar.YEAR)
        val currentMonth = calendarDate.get(Calendar.MONTH)
        val currentDay = calendarDate.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(
            requireContext(), R.style.DialogTheme, this, currentYear, currentMonth, currentDay
        )
        datePickerDialog.datePicker.minDate = calendarDate.timeInMillis
        datePickerDialog.show()

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth

        val calendarTime = Calendar.getInstance()
        val currentHour = calendarTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendarTime.get(Calendar.MINUTE)

        timePickerDialog = TimePickerDialog(
            requireContext(), R.style.DialogTheme, this, currentHour, currentMinute, false
        )
        timePickerDialog.updateTime(currentHour, currentMinute)
        timePickerDialog.show()

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val amPm = if (hourOfDay >= 12) "PM" else "AM"
        val hour = if (hourOfDay > 12) hourOfDay - 12 else hourOfDay
        val formattedTime = String.format("%02d:%02d %s", hour, minute, amPm)


        calendar[Calendar.HOUR_OF_DAY] = hourOfDay
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0


        val dateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.getDefault())
        formattedDateTime = dateFormat.format(calendar.time)

//        Toast.makeText(requireContext(), formattedDateTime, Toast.LENGTH_LONG).show()


        if (hasOverlayPermission(requireContext())) {
            setAlarm()
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:${requireContext().packageName}")
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(requireContext())) {
                    setAlarm()
                } else {
                    Toast.makeText(requireContext(), "You Did not", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }


    fun hasOverlayPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context)
        }
        return true
    }

    fun setAlarm() {

        alarmIdCounter++

        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra(Constants.alarmIdKey, alarmIdCounter)
        intent.putExtra(Constants.notificationIdKey, notif)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), alarmIdCounter, intent, 0)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
        )
        Toast.makeText(requireContext(), "Alarm Set success", Toast.LENGTH_SHORT).show()
        var address = ""
        try{
            address = Constants.placeToAlarm.address
        }catch(ex : java.lang.Exception){
            address = ""
        }
        alarmViewModel.addAlarmToFav(AlarmItem(alarmIdCounter, address , formattedDateTime))
        saveAlarmIdCounter(alarmIdCounter)

    }

    private fun getSavedAlarmIdCounter(): Int {
        val sharedPreferences =
            requireContext().getSharedPreferences("appPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("ALARM_ID_COUNTER", 0)
    }

    private fun saveAlarmIdCounter(alarmIdCounter: Int) {
        val sharedPreferences =
            requireContext().getSharedPreferences("appPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("ALARM_ID_COUNTER", alarmIdCounter)
        editor.apply()
    }

    override fun onClickDeleteAlarm(alarm: AlarmItem) {

        AlertDialog.Builder(requireContext())
            .setMessage("Do you want to cancel this alarm")
            .setCancelable(false)
            .setPositiveButton("Yes, Cancel it") { dialog, _ ->
                alarmViewModel.deleteAlarmFromFav(alarm.id)
                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                pendingIntent = PendingIntent.getBroadcast(requireContext(), alarm.id.toInt(), intent, 0)
                alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
                Toast.makeText(requireContext(), "Alarm Canceled", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No, Keep it", null)
            .show()



    }


    private fun showViews() {

        binding.noItemsTv.visibility = View.GONE
        binding.alarmRecyclerView.visibility = View.VISIBLE
    }


    private fun hideViews() {
        binding.noItemsTv.visibility = View.VISIBLE
        binding.alarmRecyclerView.visibility = View.GONE
    }


}
