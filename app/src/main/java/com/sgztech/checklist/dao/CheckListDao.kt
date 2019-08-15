package com.sgztech.checklist.dao

import androidx.room.*
import com.sgztech.checklist.model.CheckList

@Dao
interface CheckListDao {

    @Query("SELECT * FROM CHECKLIST")
    fun all(): List<CheckList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg checkList: CheckList)

    @Update
    fun update(checkList: CheckList)

    @Delete
    fun delete(checkList: CheckList)
}