package com.sgztech.checklist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sgztech.checklist.extension.toPtBrDateString
import java.util.*

@Entity
data class CheckList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val createDate: String = Date().toPtBrDateString()
)