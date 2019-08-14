package com.sgztech.checklist.extension

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.showLog(message: String) {
    Log.w("TAG_DEBUG", message)
}

fun AppCompatActivity.openActivity(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
    finish()
}
