package com.sgztech.checklist.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.sgztech.checklist.dao.CheckItemDao
import com.sgztech.checklist.model.CheckItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckItemRepository(private val dao: CheckItemDao) {

    fun insert(checkItem: CheckItem) {
        GlobalScope.launch {
            dao.add(checkItem)
            Log.w("DEBUG", "Adicionei: id: ${checkItem.id} , name: ${checkItem.name}, done: ${checkItem.isDone} , idcl: ${checkItem.idCheckList}")
        }
    }

    fun update(checkItem: CheckItem) {
        GlobalScope.launch {
            dao.update(checkItem)
            Log.w("DEBUG", "UPDATE: id: ${checkItem.id} , name: ${checkItem.name}, done: ${checkItem.isDone} , idcl: ${checkItem.idCheckList}")
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