package com.sgztech.checklist.extension

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import com.sgztech.checklist.util.SnackBarUtil

fun ComponentActivity.showLog(message: String) {
    Log.w("TAG_DEBUG", message)
}

fun ComponentActivity.openActivity(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
    finish()
}

fun ComponentActivity.showMessage(view: View, @StringRes resourceMessage: Int) {
    SnackBarUtil.show(view, resourceMessage)
}
