package com.example.lr2.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import com.zeugmasolutions.localehelper.Locales
import com.example.lr2.R
import com.example.lr2.viewModels.TabataViewModel
import com.example.lr2.viewModels.TabataViewModelFactory
import com.example.lr2.utility.TabataApp


class SettingsActivity : LocaleAwareCompatActivity() {
    private lateinit var settingsFragment: SettingsFragment
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        settingsFragment =
            SettingsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.prefs_content, settingsFragment).commit()
    }

    override fun onResume() {
        val themePreference = settingsFragment.findPreference<Preference>("dark_theme")!!
        themePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->         // THEME //
            sharedPreferences.edit().putBoolean("dark_theme", newValue as Boolean).apply()
            TabataApp.updateTheme(newValue)
            true
        }

        val localePreference = settingsFragment.findPreference<Preference>("lang")!!                 // LANGUAGE //
        localePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            if(newValue as String == "ru")
                updateLocale(Locales.Russian)
            else
                updateLocale(Locales.English)
            true
        }

        val fontSizePreference = settingsFragment.findPreference<ListPreference>("text_size")!!    // FONT //
        fontSizePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            var sizeCoef = 0f
            when (newValue as String) {
                "small" -> sizeCoef = 0.85f
                "medium" -> sizeCoef = 1.0f
                "large" -> sizeCoef = 1.15f
            }
            resources.configuration.fontScale = sizeCoef
            resources.displayMetrics.scaledDensity = resources.configuration.fontScale * resources.displayMetrics.density
            baseContext.resources.updateConfiguration(resources.configuration, DisplayMetrics())
            finish()
            startActivity(Intent(this, this::class.java))
            true
        }

        val deleteAllPreference = settingsFragment.findPreference<Preference>("delete_all")          // DELETE ALL //
        deleteAllPreference!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val alertDialog = AlertDialog.Builder(this)
            val r = resources
            val context = applicationContext
            alertDialog.setTitle(r.getString(R.string.warning))
            alertDialog.setMessage(r.getString(R.string.dialog_message))

            alertDialog.setPositiveButton(R.string.ok) { _, _ ->
                val tabataViewModel: TabataViewModel by viewModels {
                    TabataViewModelFactory((this.application as TabataApp).repository)
                }
                tabataViewModel.clear()
                Toast.makeText(context, r.getString(R.string.ok_text), Toast.LENGTH_SHORT).show()
            }
            alertDialog.setNegativeButton(R.string.no) { _, _ ->
                Toast.makeText(context, r.getString(R.string.no_text), Toast.LENGTH_SHORT).show()
            }
            alertDialog.setNeutralButton("Maybe") { _, _ ->
                Toast.makeText(context, r.getString(R.string.maybe_text), Toast.LENGTH_SHORT).show()
            }
            alertDialog.show()
            true
        }
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}