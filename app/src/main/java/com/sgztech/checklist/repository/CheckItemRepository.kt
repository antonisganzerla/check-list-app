package com.sgztech.checklist.repository

import com.sgztech.checklist.dao.CheckItemDao
import com.sgztech.checklist.model.CheckItem

class CheckItemRepository(
    private val dao: CheckItemDao,
) {

    suspend fun insert(checkItem: CheckItem) {
        dao.add(checkItem)
    }

    suspend fun update(checkItem: CheckItem) {
        dao.update(checkItem)
    }

    suspend fun delete(checkItem: CheckItem) {
        dao.delete(checkItem)
    }

    suspend fun getAllCheckItems(idCheckList: Long): List<CheckItem> {
        return dao.loadbyCheckList(idCheckList)
    }
}