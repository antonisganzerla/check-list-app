package com.sgztech.checklist.util

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.checklist.R

object CheckNameUtil {

    fun isValid(editText: EditText, textInputLayout: TextInputLayout): Boolean {
        val text = editText.text.toString()
        textInputLayout.isErrorEnabled = true
        if (text.isEmpty()) {
            textInputLayout.error = editText.context.getString(R.string.msg_name_required)
            return false
        }

        textInputLayout.error = ""
        textInputLayout.isErrorEnabled = false
        return true
    }
}