package com.example.lr2.viewModels

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.lr2.data.TabataEntity
import com.example.lr2.data.TabataRepository

class TabataViewModel(private val repo: TabataRepository) : ViewModel() {
    val allTabatas: LiveData<List<TabataEntity>> = repo.allTabatas

    fun insertTabata(tabata: TabataEntity) = viewModelScope.launch(Dispatchers.IO){
        repo.insertTabata(tabata)
    }

    fun updateTabata(tabata: TabataEntity) = viewModelScope.launch(Dispatchers.IO){
        repo.updateTabata(tabata)
    }
    fun deleteTabata(tabata: TabataEntity) = viewModelScope.launch(Dispatchers.IO){
        repo.deleteTabata(tabata)
    }
    fun clear() = viewModelScope.launch(Dispatchers.IO){
        repo.clear()
    }
}

class TabataViewModelFactory(private val repository: TabataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TabataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TabataViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
