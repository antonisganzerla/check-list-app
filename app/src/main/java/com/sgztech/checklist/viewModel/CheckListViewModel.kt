package com.sgztech.checklist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sgztech.checklist.model.CheckList
import com.sgztech.checklist.repository.CheckListRepository

class CheckListViewModel(private val repository: CheckListRepository) : ViewModel() {

    private var allCheckLists: LiveData<List<CheckList>> = repository.getAllCheckLists()

    fun insert(checkList: CheckList) {
        repository.insert(checkList)
    }

    fun delete(checkList: CheckList) {
        repository.delete(checkList)
    }

    fun getAllCheckLists(): LiveData<List<CheckList>> {
        return allCheckLists
    }
}