package com.example.newsapp.news.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FilterDialogeFragmentBinding
import com.example.newsapp.news.domain.model.FilterModel
import com.example.newsapp.news.domain.model.FilterSelectedData
import com.example.newsapp.news.presentation.NewsViewModel

class FilterDialogFragment : DialogFragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var filterDialogFragmentBinding: FilterDialogeFragmentBinding
    private lateinit var filterList: MutableList<FilterModel>
    private lateinit var filterSelectedData: FilterSelectedData
    private lateinit var adapter: FilterAdapter
    var country = ""
    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        filterDialogFragmentBinding = FilterDialogeFragmentBinding.inflate(layoutInflater)
        return filterDialogFragmentBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[NewsViewModel::class.java]
        filterList = viewModel.getFilterData().toMutableList()
        filterSelectedData = viewModel.getFilterSelectedItems()

        filterList.forEach { filterModel ->
            if (filterModel.value == filterSelectedData.country) {
                filterModel.isSelected = true
                country = filterSelectedData.country
            } else
                if (filterModel.value == filterSelectedData.category) {
                filterModel.isSelected = true
                category = filterSelectedData.category
            }
        }

        adapter = FilterAdapter(
            requireContext()
        ) { prevSelectedIndex, currentSelectedIndex ->
            val refreshList = filterList.map { it.copy() }
            if (refreshList[currentSelectedIndex].type == "country") {
                country = refreshList[currentSelectedIndex].value.toString()
                refreshList[currentSelectedIndex].isSelected = true
                if (prevSelectedIndex != -1)
                    refreshList[prevSelectedIndex].isSelected = false
            } else {
                category = refreshList[currentSelectedIndex].value
                refreshList[currentSelectedIndex].isSelected = true
                if (prevSelectedIndex != -1)
                    refreshList[prevSelectedIndex].isSelected = false
            }


            adapter.submitList(refreshList)
            filterList = refreshList.toMutableList()
        }

        val layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        filterDialogFragmentBinding.filterList.layoutManager = layoutManager
        filterDialogFragmentBinding.filterList.setHasFixedSize(true)
        filterDialogFragmentBinding.filterList.adapter = adapter

        adapter.submitList(filterList)

        filterDialogFragmentBinding.okButton.setOnClickListener {
            viewModel.changeFilterData(country, category)
            dismiss()
        }

    }

}