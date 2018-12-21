package com.sriram.wally.core

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class BootReciever() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {


            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val alarmIntent = Intent(context, WallyService::class.java).apply {
                action = WallyService.ACTION_SCHEDULE_WALLPAPER
            }.let {
                PendingIntent.getService(context, 0, it, 0)
            }

            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 9)
            }

            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    alarmIntent
            )
        }
    }
}