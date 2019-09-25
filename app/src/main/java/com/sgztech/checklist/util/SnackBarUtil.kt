package com.sgztech.checklist.util

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

object SnackBarUtil{

    fun show(view: View, @StringRes resourceMessage: Int){
        Snackbar.make(view, resourceMessage, Snackbar.LENGTH_LONG).show()
    }
}