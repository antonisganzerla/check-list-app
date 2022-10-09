package com.sgztech.checklist.repository

import com.sgztech.checklist.dao.CheckListDao
import com.sgztech.checklist.model.CheckList

class CheckListRepository(private val dao: CheckListDao) {

    suspend fun insert(checkList: CheckList) {
        dao.add(checkList)
    }

    suspend fun delete(checkList: CheckList) {
        dao.delete(checkList)
    }

    suspend fun getAllCheckLists(): List<CheckList> =
        dao.all()
}