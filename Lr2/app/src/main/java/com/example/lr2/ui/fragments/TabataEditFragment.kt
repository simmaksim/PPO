package com.example.lr2.ui.fragments

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.example.lr2.R
import com.example.lr2.viewModels.TabataViewModel
import com.example.lr2.viewModels.TabataViewModelFactory
import com.example.lr2.databinding.FragmentTabataEditBinding
import com.example.lr2.utility.TabataApp
import com.example.lr2.viewModels.EditTabataViewModel


class TabataEditFragment : Fragment(){

    private val binding by lazy { FragmentTabataEditBinding.inflate(layoutInflater) }
    private val viewModel: EditTabataViewModel by activityViewModels()
    private val tabataViewModel: TabataViewModel by viewModels {
        TabataViewModelFactory((activity?.application as TabataApp).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.selectColor.setBackgroundColor(resources.getColor(R.color.green_700))
        viewModel.setInputFilters(binding)
        viewModel.renderTabataEdit(binding)

        binding.buttonSave.setOnClickListener {
            viewModel.saveTabata(binding, tabataViewModel)
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        binding.selectColor.setOnClickListener {
            createColorPickerDialog((binding.selectColor.background as ColorDrawable).color)
        }
    }

    private fun createColorPickerDialog(defaultColor: Int){
        context?.let {
            MaterialColorPickerDialog
                .Builder(it)
                .setColorShape(ColorShape.SQAURE)
                .setColorSwatch(ColorSwatch._700)
                .setDefaultColor(defaultColor)
                .setColorListener { color, _ -> binding.selectColor.setBackgroundColor(color) }
                .show()
        }
    }
}