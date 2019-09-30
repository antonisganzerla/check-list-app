package com.sgztech.checklist.util

import android.content.Context
import android.preference.PreferenceManager

object PreferenceUtil {

    fun getUserId(context: Context): String{
        val value = sharedPreferences(context).getString(KEY_USER_ID, DEFAULT_STRING_VALUE)
        return value ?: DEFAULT_STRING_VALUE
    }

    fun setUserId(context: Context, value: String){
        val editor = sharedPreferences(context).edit()
        editor.putString(KEY_USER_ID, value)
        editor.apply()
    }

    @JvmStatic
    private fun sharedPreferences(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)

    private const val DEFAULT_STRING_VALUE = ""
    private const val KEY_USER_ID = "KEY_USER_ID"
}