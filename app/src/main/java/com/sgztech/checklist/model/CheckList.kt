package com.sgztech.checklist.model

import java.util.*

data class CheckList(
    val name: String,
    val createDate: Date = Date()
)