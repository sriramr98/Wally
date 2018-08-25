package com.sriram.wally.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.jetbrains.anko.intentFor

class AlarmReciever: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = context?.intentFor<WallyService>()
        serviceIntent?.action = WallyService.ACTION_SCHEDULE_WALLPAPER
        context?.startService(serviceIntent)
    }

}