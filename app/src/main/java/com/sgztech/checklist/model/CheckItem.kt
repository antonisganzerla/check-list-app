package com.sgztech.checklist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CheckItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    var isDone: Boolean = false,
    val idCheckList: Long)
