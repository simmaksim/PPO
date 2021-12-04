package com.example.lr2.data

import android.content.Context
import android.graphics.Color
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.lr2.R
import com.example.lr2.R.color.green_700

@Database(entities = [TabataEntity::class], version = 2, exportSchema = false)
abstract class TabataDatabase : RoomDatabase() {

    abstract fun tabataDao(): TabataDAO

    companion object { @Volatile private var INSTANCE: TabataDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TabataDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TabataDatabase::class.java,
                    context.getString(R.string.db_name)
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(TabataDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return@synchronized instance
            }
        }
    }

    private class TabataDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.tabataDao())
                }
            }
        }

        fun populateDatabase(tabataDao: TabataDAO) {
            tabataDao.clear()

            val tabata = TabataEntity(
                 "Tabata classic", "#388E3C", 60,
                20, 10, 4, 2, 15)
            tabataDao.insertTabata(tabata)
        }
    }

}
