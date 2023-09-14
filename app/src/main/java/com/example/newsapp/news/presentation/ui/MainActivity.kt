package com.example.newsapp.news.presentation.ui

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.core.remote.retrofit.Status
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

//        viewModel.filterDataChanged.observe(this, Observer {
//            if (it)
//                adapter.refresh()
//        }).

        mainBinding.headerLayout.backButton.setOnClickListener {
            supportFragmentManager.popBackStack()
            mainBinding.headerLayout.backButton.visibility = View.GONE
            mainBinding.headerLayout.filterButton.visibility = View.VISIBLE
        }

        mainBinding.headerLayout.filterButton.setOnClickListener {
            val filterDialogeFragment = FilterDialogeFragment()
            filterDialogeFragment.show(supportFragmentManager, "filter dialoge")
        }
    }

    override fun onBackPressed() {
        super.onBackPressedDispatcher.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            mainBinding.headerLayout.backButton.visibility = View.GONE
            mainBinding.headerLayout.filterButton.visibility = View.VISIBLE
        }

    }
}