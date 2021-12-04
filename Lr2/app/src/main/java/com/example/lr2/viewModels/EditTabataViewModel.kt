package com.example.lr2.viewModels

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputFilter
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lr2.data.TabataEntity
import com.example.lr2.databinding.FragmentTabataEditBinding
import com.example.lr2.utility.TimeInputFilter

class EditTabataViewModel: ViewModel() {

    private var tabata = MutableLiveData<TabataEntity>()
    var newTabata = false

    fun setTabata(tabata: TabataEntity) {
        this.tabata.value = tabata
        newTabata = false
    }

    fun renderTabataEdit(binding: FragmentTabataEditBinding){
        if (newTabata) return
        val curr: TabataEntity = tabata.value!!
        binding.tabataTitle.setText(curr.name, TextView.BufferType.EDITABLE)
        binding.warmUpMin.setText(getMinutes(curr.warm_up), TextView.BufferType.EDITABLE)
        binding.warmUpS.setText(getSec(curr.warm_up), TextView.BufferType.EDITABLE)
        binding.workMin.setText(getMinutes(curr.work), TextView.BufferType.EDITABLE)
        binding.workS.setText(getSec(curr.work), TextView.BufferType.EDITABLE)
        binding.restMin.setText(getMinutes(curr.rest), TextView.BufferType.EDITABLE)
        binding.restS.setText(getSec(curr.rest), TextView.BufferType.EDITABLE)
        binding.cooldownMin.setText(getMinutes(curr.cooldown), TextView.BufferType.EDITABLE)
        binding.cooldownS.setText(getSec(curr.cooldown), TextView.BufferType.EDITABLE)
        binding.repeats.setText(curr.repeats.toString(), TextView.BufferType.EDITABLE)
        binding.cycles.setText(curr.cycles.toString(), TextView.BufferType.EDITABLE)
        binding.repeats.setText(curr.repeats.toString(), TextView.BufferType.EDITABLE)
        binding.selectColor.setBackgroundColor(Color.parseColor(curr.color))
    }

    fun setInputFilters(binding: FragmentTabataEditBinding) {
        val filters = arrayOf<InputFilter>(TimeInputFilter())
        binding.warmUpMin.filters = filters
        binding.warmUpS.filters = filters
        binding.workMin.filters = filters
        binding.workS.filters = filters
        binding.restMin.filters = filters
        binding.restS.filters = filters
        binding.cooldownMin.filters = filters
        binding.cooldownS.filters = filters
    }

    fun saveTabata(binding: FragmentTabataEditBinding, viewModel: TabataViewModel) {
        val name = binding.tabataTitle.text.toString()
        val warm_up = binding.warmUpMin.text.toString().toInt() * 60 + binding.warmUpS.text.toString().toInt()
        val work = binding.workMin.text.toString().toInt() * 60 + binding.workS.text.toString().toInt()
        val rest = binding.restMin.text.toString().toInt() * 60 + binding.restS.text.toString().toInt()
        val cooldown = binding.cooldownMin.text.toString().toInt() * 60 + binding.cooldownS.text.toString().toInt()
        val repeats = if (binding.repeats.text.toString() == "") 0 else binding.repeats.text.toString().toInt()
        val cycles = if(binding.cycles.text.toString() == "") 0 else binding.cycles.text.toString().toInt()
        val color = java.lang.String.format("#%06X", 0xFFFFFF and (binding.selectColor.background as ColorDrawable).color)

        val currTabata = TabataEntity(name, color, warm_up, work, rest, repeats, cycles, cooldown)
        if(newTabata)
            viewModel.insertTabata(currTabata)
        else {
            currTabata.id = tabata.value!!.id
            viewModel.updateTabata(currTabata)
        }
    }

    companion object Time {
        fun getTime(time: Int) : String = (getMinutes(time) + ":" + getSec(time))
        fun getMinutes(time: Int): String = addZero(time / 60)
        fun getSec(time: Int): String = addZero(time - (time / 60).toInt() * 60)
        private fun addZero(time: Int): String = if (time < 10) { "0$time" } else { "$time" }
    }
}