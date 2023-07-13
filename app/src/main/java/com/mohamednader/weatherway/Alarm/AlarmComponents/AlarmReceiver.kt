package com.mohamednader.weatherway.Alarm.AlarmComponents

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mohamednader.weatherway.MainHome.View.MainHome
import com.mohamednader.weatherway.R
import com.mohamednader.weatherway.Utilities.Constants

class AlarmReceiver : BroadcastReceiver() {

    private var ringtone: Ringtone? = null
    private val TAG = "AlarmReceiver_INFO_TAG"
    lateinit var notificationManagerCompat: NotificationManagerCompat
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {


        val alarmId = intent.getIntExtra(Constants.alarmIdKey, 0)
        val notif = intent.getStringExtra(Constants.notificationIdKey)

        Log.i(TAG, "onReceive: $alarmId")
        Log.i(TAG, "onReceive: $notif")

        val nextActivity = Intent(context, MainHome::class.java)
        nextActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, alarmId, nextActivity, 0)


        if(notif == Constants.notification_enable){
            val builder = NotificationCompat.Builder(context, "alarms")
                .setSmallIcon(R.drawable.cloud)
                .setContentTitle(context.resources.getText(R.string.weather_is_fine))
                .setContentText(context.resources.getText(R.string.dismiss))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            notificationManagerCompat = NotificationManagerCompat.from(context)
            val notificationId = Constants.notificationIdPrefix + alarmId
            notificationManagerCompat.notify(Constants.notificationIdPrefix, alarmId, builder.build())

        }

        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone?.play()

        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.fragment_alarm_dialog, null)

         val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

         val dismissButton = dialogView.findViewById<Button>(R.id.dismissButton)
        dismissButton.setOnClickListener {

            alertDialog.dismiss()
            ringtone?.stop()
            if(notif == Constants.notification_enable){
                notificationManagerCompat.cancel(Constants.notificationIdPrefix, alarmId)
            }

            try {
                pendingIntent.send()
            } catch (e: PendingIntent.CanceledException) {
                e.printStackTrace()
            }
        }

        val updateIntent = Intent("com.mohamednader.weatherway.ACTION_ALARM")
        updateIntent.putExtra(Constants.alarmIdKey, alarmId)


        alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        alertDialog.show()
    }
}
