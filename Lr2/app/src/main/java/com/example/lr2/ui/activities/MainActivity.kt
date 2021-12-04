package com.example.lr2.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import com.example.lr2.R
import com.example.lr2.databinding.ActivityMainBinding
import com.example.lr2.viewModels.EditTabataViewModel

class MainActivity : LocaleAwareCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: EditTabataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Tabata_NoActionBar)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(EditTabataViewModel::class.java)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)
        setTitle(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish()
        return super.onOptionsItemSelected(item)
    }
}