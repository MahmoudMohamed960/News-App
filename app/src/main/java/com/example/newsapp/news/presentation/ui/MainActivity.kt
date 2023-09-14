package com.example.newsapp.news.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.news.presentation.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        mainBinding.headerLayout.backButton.visibility = View.GONE
        mainBinding.headerLayout.filterButton.visibility = View.VISIBLE

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, HomeFragment())
        transaction.commit()

        viewModel.selectedArticle.observe(this) {
            mainBinding.headerLayout.backButton.visibility = View.VISIBLE
            mainBinding.headerLayout.filterButton.visibility = View.GONE

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, DetailsFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        viewModel.headerTitle.observe(this) { title ->

            mainBinding.headerLayout.title.text = title ?: resources.getString(R.string.all_news)
        }

        mainBinding.headerLayout.backButton.setOnClickListener {
            supportFragmentManager.popBackStack()
            mainBinding.headerLayout.backButton.visibility = View.GONE
            mainBinding.headerLayout.filterButton.visibility = View.VISIBLE
        }

        mainBinding.headerLayout.filterButton.setOnClickListener {
            val filterDialogFragment = FilterDialogFragment()
            filterDialogFragment.show(supportFragmentManager, "filter dialog")
        }
    }

    override fun onStart() {
        super.onStart()
        mainBinding.headerLayout.title.text = resources.getString(R.string.all_news)
    }

    override fun onBackPressed() {
        super.onBackPressedDispatcher.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            mainBinding.headerLayout.backButton.visibility = View.GONE
            mainBinding.headerLayout.filterButton.visibility = View.VISIBLE
        }

    }
}