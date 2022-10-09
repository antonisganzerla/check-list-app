package com.sgztech.checklist.dao

import androidx.room.*
import com.sgztech.checklist.model.CheckList

@Dao
interface CheckListDao {

    @Query("SELECT * FROM CHECKLIST")
    suspend fun all(): List<CheckList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(vararg checkList: CheckList)

    @Update
    suspend fun update(checkList: CheckList)

    @Delete
    suspend fun delete(checkList: CheckList)
}