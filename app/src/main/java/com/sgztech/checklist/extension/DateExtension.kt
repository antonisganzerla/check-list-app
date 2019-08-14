package com.sgztech.checklist.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.toPtBrDateString(): String {
    return SimpleDateFormat("dd/MM/yy HH:mm").format(this)
}