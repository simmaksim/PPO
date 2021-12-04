package com.example.lr2.data

import androidx.lifecycle.LiveData

class TabataRepository(private val tabataDao : TabataDAO) {
    val allTabatas : LiveData<List<TabataEntity>> = tabataDao.getTabatas()

    fun insertTabata(tabata: TabataEntity){
        tabataDao.insertTabata(tabata)
    }
    fun updateTabata(tabata: TabataEntity){
        tabataDao.updateTabata(tabata)
    }
    fun deleteTabata(tabata: TabataEntity){
        tabataDao.deleteTabata(tabata)
    }
    fun clear(){
        tabataDao.clear()
    }

}