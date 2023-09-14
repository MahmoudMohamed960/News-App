package com.example.newsapp.news.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.news.presentation.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    lateinit var homeBinding: FragmentHomeBinding
    lateinit var adapter: ArticlesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[NewsViewModel::class.java]
        loadArticles()
        filterArticles()

        homeBinding.refreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun filterArticles() {
        viewModel.filterDataChanged.observe(viewLifecycleOwner) {
            adapter.refresh()
        }
    }

    private fun loadArticles() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.articlesFlow.collectLatest { articles ->
                adapter = ArticlesAdapter(requireContext()) { article ->
                    viewModel.changeSelectedArticle(article)
                }
                val layoutManager = LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                homeBinding.newsList.layoutManager = layoutManager
                homeBinding.newsList.adapter = adapter
                homeBinding.refreshLayout.isRefreshing = false
                adapter.submitData(articles)

                adapter.loadStateFlow.collectLatest { loadState ->
                    val errorMessage = when {
                        loadState.refresh is LoadState.Error -> {
                            "Error: ${(loadState.refresh as LoadState.Error).error.message}"
                        }
                        loadState.append is LoadState.Error -> {
                            "Error: ${(loadState.append as LoadState.Error).error.message}"
                        }
                        loadState.prepend is LoadState.Error -> {
                            "Error: ${(loadState.prepend as LoadState.Error).error.message}"
                        }
                        else -> null
                    }

                    if (errorMessage != null) {
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                }

            }

        }
    }

}