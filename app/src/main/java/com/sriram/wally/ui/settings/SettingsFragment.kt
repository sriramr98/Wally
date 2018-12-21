package com.sriram.wally.ui.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceFragment
import com.sriram.wally.R
import com.sriram.wally.core.WallyService
import java.util.*

class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        private const val KEY_RANDOM_WALLPAPER_JOB = "key_random_wallpaper_job"
        private const val TAG_WALLPAPER_WORK = "tag-wallpaper-work"
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences == null || key == null) return
        if (key == getString(R.string.key_regular_wallpaper)) {
            scheduleOrCancelWallpaperWork(sharedPreferences, key)
        }
    }

    private fun scheduleOrCancelWallpaperWork(pref: SharedPreferences, key: String) {
        val shouldScheduleJob = pref.getBoolean(key, false)
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(activity, WallyService::class.java).apply {
            action = WallyService.ACTION_SCHEDULE_WALLPAPER
        }.let {
            PendingIntent.getService(activity, 0, it, 0)
        }
        if (shouldScheduleJob) {
            // Set the alarm to start at approximately 9:00 AM.
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
        } else {
            // cancel the existing job
            alarmManager.cancel(alarmIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}