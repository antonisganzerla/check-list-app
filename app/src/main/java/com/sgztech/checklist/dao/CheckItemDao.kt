package com.sgztech.checklist.dao

import androidx.room.*
import com.sgztech.checklist.model.CheckItem

@Dao
interface CheckItemDao {

    @Query("SELECT * FROM CHECKITEM")
    fun all(): List<CheckItem>

    @Query("SELECT * FROM CHECKITEM WHERE IDCHECKLIST LIKE :idCheckList")
    fun loadbyCheckList(idCheckList: Long): List<CheckItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg checkItem: CheckItem)

    @Update
    fun update(checkItem: CheckItem)

    @Delete
    fun delete(checkItem: CheckItem)
}