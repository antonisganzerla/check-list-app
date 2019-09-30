package com.sgztech.checklist.repository

import androidx.lifecycle.LiveData
import com.sgztech.checklist.dao.CheckItemDao
import com.sgztech.checklist.model.CheckItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckItemRepository(private val dao: CheckItemDao) {

    fun insert(checkItem: CheckItem) {
        GlobalScope.launch {
            dao.add(checkItem)
        }
    }

    fun update(checkItem: CheckItem) {
        GlobalScope.launch {
            dao.update(checkItem)
        }
    }

    fun delete(checkItem: CheckItem) {
        GlobalScope.launch {
            dao.delete(checkItem)
        }
    }

    fun getAllCheckItens(idCheckList: Long): LiveData<List<CheckItem>> {
        return dao.loadbyCheckList(idCheckList)
    }
}