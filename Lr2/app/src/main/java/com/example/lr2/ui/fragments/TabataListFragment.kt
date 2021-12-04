package com.example.lr2.ui.fragments

import android.icu.text.DateTimePatternGenerator.PatternInfo.OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lr2.R
import com.example.lr2.data.TabataEntity
import com.example.lr2.viewModels.TabataViewModel
import com.example.lr2.viewModels.TabataViewModelFactory
import com.example.lr2.databinding.FragmentTabataListBinding
import com.example.lr2.utility.TabataApp
import com.example.lr2.utility.TabataListAdapter
import com.example.lr2.viewModels.EditTabataViewModel

class TabataListFragment : Fragment(), TabataListAdapter.RecyclerViewLongClickListener {

    private val binding: FragmentTabataListBinding by lazy { FragmentTabataListBinding.inflate(layoutInflater) }
    private val viewModel: EditTabataViewModel by activityViewModels()
    private val tabataViewModel: TabataViewModel by viewModels {
        TabataViewModelFactory((activity?.application as TabataApp).repository)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TabataListAdapter (tabataViewModel.allTabatas.value, this) { tabata -> tabataItemClicked(tabata) }
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)

        tabataViewModel.allTabatas.observe(viewLifecycleOwner, Observer { tabatas ->
            tabatas?.let { adapter.submitList(it) }
        })
        binding.fab.setOnClickListener {
            viewModel.newTabata = true
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun tabataItemClicked(tabata: TabataEntity) {
        viewModel.setTabata(tabata)
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    override fun recyclerViewListLongClicked(tabata: TabataEntity) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(resources.getString(R.string.warning))
        alertDialog.setMessage(resources.getString(R.string.delete_tabata_message))

        alertDialog.setPositiveButton(R.string.ok1) { _, _ ->
            tabataViewModel.deleteTabata(tabata)
            Toast.makeText(context, resources.getString(R.string.ok1_text), Toast.LENGTH_SHORT).show()
        }
        alertDialog.setNegativeButton(R.string.no1) { _, _ ->
            Toast.makeText(context, resources.getString(R.string.no1_text), Toast.LENGTH_SHORT).show()
        }
        alertDialog.show()
    }
}