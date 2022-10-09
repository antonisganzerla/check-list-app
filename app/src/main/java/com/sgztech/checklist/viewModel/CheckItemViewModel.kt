package com.sgztech.checklist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgztech.checklist.model.CheckItem
import com.sgztech.checklist.repository.CheckItemRepository
import kotlinx.coroutines.launch

class CheckItemViewModel(
    private val repository: CheckItemRepository,
) : ViewModel() {

    private val _checkItems: MutableLiveData<List<CheckItem>> = MutableLiveData()
    val checkItems: LiveData<List<CheckItem>> = _checkItems

    fun insert(checkItem: CheckItem) {
        viewModelScope.launch {
            repository.insert(checkItem)
        }
    }

    fun update(checkItem: CheckItem) {
        viewModelScope.launch {
            repository.update(checkItem)
        }
    }

    fun delete(checkItem: CheckItem) {
        viewModelScope.launch {
            repository.delete(checkItem)
        }
    }

    fun getAllCheckItems(idCheckList: Long) {
        viewModelScope.launch {
            _checkItems.postValue(repository.getAllCheckItems(idCheckList))
        }
    }

    fun filter(newText: String?, idCheckList: Long) {
        if (newText.isNullOrEmpty())
            getAllCheckItems(idCheckList)
        else
            viewModelScope.launch {
                _checkItems.postValue(
                    repository.getAllCheckItems(idCheckList)
                        .filter { it.name.startsWith(newText, ignoreCase = true) })
            }
    }
}