package com.sriram.wally.ui.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import com.sriram.wally.R

class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
    }
}