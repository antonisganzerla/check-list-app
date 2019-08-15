package com.sgztech.checklist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sgztech.checklist.dao.CheckItemDao
import com.sgztech.checklist.dao.CheckListDao
import com.sgztech.checklist.model.CheckItem
import com.sgztech.checklist.model.CheckList

@Database(entities = [CheckList::class, CheckItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun checkListDao(): CheckListDao
    abstract fun checkItemDao(): CheckItemDao
}