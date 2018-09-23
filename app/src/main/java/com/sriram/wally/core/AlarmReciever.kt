package com.sriram.wally.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.jetbrains.anko.intentFor

class AlarmReciever: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val randomWallPaperIntent = context?.intentFor<WallyService>()
        randomWallPaperIntent?.action = WallyService.ACTION_RANDOM_WALLPAPER
        context?.startService(randomWallPaperIntent)
    }

}