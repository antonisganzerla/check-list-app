package com.sgztech.checklist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CheckItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val isDone: Boolean = false,
    val idCheckList: Long)
