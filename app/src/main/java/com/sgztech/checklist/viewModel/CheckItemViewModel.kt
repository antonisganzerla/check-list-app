package com.sgztech.checklist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sgztech.checklist.model.CheckItem
import com.sgztech.checklist.repository.CheckItemRepository

class CheckItemViewModel(private val repository: CheckItemRepository) : ViewModel() {

    fun insert(checkItem: CheckItem) {
        repository.insert(checkItem)
    }

    fun update(checkItem: CheckItem) {
        repository.update(checkItem)
    }

    fun delete(checkItem: CheckItem) {
        repository.delete(checkItem)
    }

    fun getAllCheckItens(idCheckList: Long): LiveData<List<CheckItem>> {
        return repository.getAllCheckItens(idCheckList)
    }
}