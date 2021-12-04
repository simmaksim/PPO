package com.example.lr2.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TabataDAO {
    @Query("SELECT * FROM TabataEntity")
    fun getTabatas() : LiveData<List<TabataEntity>>

    @Query("SELECT * FROM TabataEntity WHERE id = :id")
    fun getTabata(id : Int) : LiveData<TabataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTabata(tabata: TabataEntity)

    @Update
    fun updateTabata(tabata: TabataEntity)

    @Delete
    fun deleteTabata(tabata: TabataEntity)

    @Query("DELETE FROM TabataEntity")
    fun clear()
}