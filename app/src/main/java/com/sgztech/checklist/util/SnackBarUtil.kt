package com.sgztech.checklist.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackBarUtil{

    @JvmStatic
    fun show(view: View, resourceMessage: Int){
        Snackbar.make(view, resourceMessage, Snackbar.LENGTH_LONG).show()
    }
}