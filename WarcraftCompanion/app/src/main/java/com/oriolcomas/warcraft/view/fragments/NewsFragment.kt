package com.oriolcomas.warcraft.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.News
import com.oriolcomas.warcraft.view.adapter.NewsAdapter
import com.oriolcomas.warcraft.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news.*

/**
 * A simple [Fragment] subclass.
 */
class NewsFragment : Fragment() {

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.fragment_news, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)

        viewModel.refresh()

        newsAdapter = NewsAdapter()

        rvNews.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = newsAdapter
        }
        observeViewModel()

    }

    fun observeViewModel()
    {
        viewModel.listNews.observe(this, Observer<List<News>>{ news -> newsAdapter.updateData(news)})
        viewModel.isLoading.observe(this, Observer<Boolean> {
            if (it != null)
                rlBaseNews.visibility = View.INVISIBLE
        })
    }


}
