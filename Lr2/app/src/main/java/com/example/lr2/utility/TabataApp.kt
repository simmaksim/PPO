package com.example.lr2.utility

import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.zeugmasolutions.localehelper.LocaleAwareApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import com.example.lr2.data.TabataDatabase
import com.example.lr2.data.TabataRepository
import java.util.*

class TabataApp : LocaleAwareApplication() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { TabataDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TabataRepository(database.tabataDao()) }

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        // set default locale for first launch //
        if (sharedPreferences.getBoolean("first", true))
            Locale.setDefault(Locale("ru"))
        sharedPreferences.edit().putBoolean("first", false).apply()
        //-------------------------------------//
        val darkTheme: Boolean = sharedPreferences.getBoolean("dark_theme", false)
        updateTheme(darkTheme)
    }

    companion object {
        fun updateTheme(darkTheme: Boolean) {
            if (darkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}
