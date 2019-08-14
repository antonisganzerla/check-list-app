package com.sgztech.checklist.view


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.sgztech.checklist.R

class PreferencesFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
