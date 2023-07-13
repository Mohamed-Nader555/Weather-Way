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
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mohamednader.weatherway.MainHome.View.MainHome
import com.mohamednader.weatherway.Model.AlarmItem
import com.mohamednader.weatherway.R
import com.mohamednader.weatherway.Utilities.AlarmViewModelHolder
import com.mohamednader.weatherway.Utilities.Constants

class AlarmReceiver : BroadcastReceiver() {

    private var ringtone: Ringtone? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {


        val alarmViewModel = AlarmViewModelHolder.alarmViewModel


        val alarmItem = intent.getSerializableExtra(Constants.alarmIdKey) as? AlarmItem

        if (alarmItem != null) {


            val nextActivity = Intent(context, MainHome::class.java)
            nextActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, alarmItem.id, nextActivity, 0)

            // Create a notification with the dismiss button
            val builder = NotificationCompat.Builder(context, "alarms")
                .setSmallIcon(R.drawable.cloud)
                .setContentTitle("Alarm Ringing")
                .setContentText("Tap to dismiss")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val notificationId = Constants.notificationIdPrefix + alarmItem.id
            notificationManagerCompat.notify(
                Constants.notificationIdPrefix,
                alarmItem.id,
                builder.build()
            )

            // Play the default alarm ringtone using Ringtone
            val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(context, alarmUri)
            ringtone?.play()

            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.fragment_alarm_dialog, null)

            // Show the custom dialog
            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(false)
                .create()

            // Find the dismiss button in the custom dialog layout
            val dismissButton = dialogView.findViewById<Button>(R.id.dismissButton)
            dismissButton.setOnClickListener {
                // Dismiss the dialog, stop the Ringtone, and cancel the notification
                alertDialog.dismiss()
                ringtone?.stop()
                notificationManagerCompat.cancel(Constants.notificationIdPrefix, alarmItem.id)
                alarmViewModel.deleteAlarmFromFav(alarmItem)
                // Open the pending intent
                try {
                    pendingIntent.send()
                } catch (e: PendingIntent.CanceledException) {
                    e.printStackTrace()
                }
            }

            // Set the dialog window type to allow it to show over other apps
            alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            alertDialog.show()
        }
    }
}
