package com.example.newsapp.news.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentDetailsBinding
import com.example.newsapp.news.presentation.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    lateinit var fragmentDetailsBinding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentDetailsBinding = FragmentDetailsBinding.inflate(layoutInflater)
        return fragmentDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[NewsViewModel::class.java]
        val article = viewModel.selectedArticle.value
        fragmentDetailsBinding.content.text = article?.content
        fragmentDetailsBinding.detailsCard.title.text = article?.title
        fragmentDetailsBinding.detailsCard.source.text = article?.source
        fragmentDetailsBinding.detailsCard.publishedAt.text = article?.newPublishedAt
        Glide.with(requireActivity()).load(article?.imageUrl)
            .into(fragmentDetailsBinding.articleImage)
    }
}