package com.sgztech.checklist.dao

import androidx.room.*
import com.sgztech.checklist.model.CheckItem

@Dao
interface CheckItemDao {

    @Query("SELECT * FROM CHECKITEM")
    suspend fun all(): List<CheckItem>

    @Query("SELECT * FROM CHECKITEM WHERE IDCHECKLIST LIKE :idCheckList")
    suspend fun loadbyCheckList(idCheckList: Long): List<CheckItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(vararg checkItem: CheckItem)

    @Update
    suspend fun update(checkItem: CheckItem)

    @Delete
    suspend fun delete(checkItem: CheckItem)
}