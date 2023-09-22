package com.rsa.newsrsa.ui.readlater

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsa.newsrsa.R
import com.rsa.newsrsa.adapter.ArticleReadLaterListAdapter
import com.rsa.newsrsa.adapter.LoaderAdapter
import com.rsa.newsrsa.data.repository.NewsLocalRepository
import com.rsa.newsrsa.databinding.FragmentReadLaterNewsBinding
import com.rsa.newsrsa.utils.ResourcesProvider
import com.rsa.newsrsa.viewmodel.NewsReadLaterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsReadLaterFragment : Fragment() {
    private lateinit var binding: FragmentReadLaterNewsBinding
    private val model: NewsReadLaterViewModel by viewModels()

    @Inject
    lateinit var newsLocalRepository: NewsLocalRepository

    @Inject
    lateinit var resourcesProvider: ResourcesProvider
    private val articleListAdapter by lazy {
        ArticleReadLaterListAdapter(newsLocalRepository, resourcesProvider,findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_read_later_news, container, false)
        binding.lifecycleOwner = this
        initializeList()
        initObservers()
        return binding.root
    }

    private fun initializeList() {
        binding.localNewsList.layoutManager = LinearLayoutManager(activity)
        binding.localNewsList.setHasFixedSize(true)
        binding.localNewsList.adapter = articleListAdapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(), footer = LoaderAdapter()
        )
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            model.getArticleReadLaterList().observe(viewLifecycleOwner) {
                articleListAdapter.submitData(lifecycle, it)
            }
        }
    }
}