package com.sgztech.checklist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgztech.checklist.model.CheckList
import com.sgztech.checklist.repository.CheckListRepository
import kotlinx.coroutines.launch

class CheckListViewModel(
    private val repository: CheckListRepository,
) : ViewModel() {

    private val _checkLists: MutableLiveData<List<CheckList>> = MutableLiveData()
    val checkLists: LiveData<List<CheckList>> = _checkLists

    fun insert(checkList: CheckList) {
        viewModelScope.launch {
            repository.insert(checkList)
        }
    }

    fun delete(checkList: CheckList) {
        viewModelScope.launch {
            repository.delete(checkList)
        }
    }

    fun loadCheckLists() {
        viewModelScope.launch {
            _checkLists.postValue(repository.getAllCheckLists())
        }
    }
}