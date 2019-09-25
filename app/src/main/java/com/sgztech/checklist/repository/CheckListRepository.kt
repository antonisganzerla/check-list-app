package com.sgztech.checklist.repository

import androidx.lifecycle.LiveData
import com.sgztech.checklist.dao.CheckListDao
import com.sgztech.checklist.model.CheckList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckListRepository(private val dao: CheckListDao) {

    private val allCheckLists: LiveData<List<CheckList>> = dao.all()

    fun insert(checkList: CheckList) {
        GlobalScope.launch {
            dao.add(checkList)
        }
    }

    fun delete(checkList: CheckList) {
        GlobalScope.launch {
            dao.delete(checkList)
        }
    }

    fun getAllCheckLists(): LiveData<List<CheckList>> {
        return allCheckLists
    }
}