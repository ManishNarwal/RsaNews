package com.rsa.newsrsa.ui.news

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
import com.rsa.newsrsa.adapter.ArticleListAdapter
import com.rsa.newsrsa.adapter.LoaderAdapter
import com.rsa.newsrsa.common.PreferencesHelper
import com.rsa.newsrsa.data.repository.NewsLocalRepository
import com.rsa.newsrsa.databinding.FragmentLocalNewsBinding
import com.rsa.newsrsa.utils.ResourcesProvider
import com.rsa.newsrsa.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocalNewsFragment : Fragment() {
    private lateinit var binding : FragmentLocalNewsBinding
    private val model: NewsViewModel by viewModels()
    @Inject
    lateinit var newsLocalRepository: NewsLocalRepository
    @Inject
    lateinit var resourcesProvider: ResourcesProvider
    private val articleListAdapter by lazy {
        ArticleListAdapter(newsLocalRepository, resourcesProvider, findNavController())
    }
    @Inject
    lateinit var preferencesHelper: PreferencesHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_local_news,container,false)
        binding.model = model
        binding.lifecycleOwner = this
        preferencesHelper.savePreferenceString("country","in")
        initializeList()
        initObservers()
        return binding.root
    }

    private fun initializeList() {
        binding.localNewsList.layoutManager = LinearLayoutManager(activity)
        binding.localNewsList.setHasFixedSize(true)
        binding.localNewsList.adapter = articleListAdapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            model.getArticleList("in").observe(viewLifecycleOwner) {
                articleListAdapter.submitData(lifecycle, it)
            }
        }
    }
//    private fun initObservers() {
//        model.articles.observe(this, Observer {
//            Timber.d("list: " + it?.size)
//            progressBar.visibility = View.GONE
//            adapter.submitList(it)
//            showEmptyText(it?.size == 0)
//        })
//
//        model.networkError?.observe(this, Observer {
//            progressBar.visibility = View.GONE
//            activity?.showToast(resources.getString(R.string.no_results))
//        })
//    }

    private fun showEmptyText(show: Boolean) {
        if (show) {
            binding.localNewsList.visibility = View.GONE
            binding.emptyText.visibility = View.VISIBLE
        } else {
            binding.localNewsList.visibility = View.VISIBLE
            binding.emptyText.visibility = View.GONE
        }
    }
}