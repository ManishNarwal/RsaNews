package com.rsa.newsrsa.ui.readmore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsa.newsrsa.adapter.ArticleAdapter
import com.rsa.newsrsa.common.PreferencesHelper
import com.rsa.newsrsa.data.repository.NewsLocalRepository
import com.rsa.newsrsa.databinding.FragmentReadMoreBinding
import com.rsa.newsrsa.utils.ResourcesProvider
import com.rsa.newsrsa.viewmodel.NewsReadMoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReadMoreFragment : Fragment() {
    private lateinit var binding : FragmentReadMoreBinding
    private val model: NewsReadMoreViewModel by viewModels()
    @Inject
    lateinit var newsLocalRepository: NewsLocalRepository
    @Inject
    lateinit var resourcesProvider: ResourcesProvider
    private lateinit var articleListAdapter : ArticleAdapter
    @Inject
    lateinit var preferencesHelper: PreferencesHelper
    private var newId : String = ""
    private val args : ReadMoreFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReadMoreBinding.inflate(inflater,container,false)
        newId = args.newId.toString()
        binding.localNewsList.layoutManager = LinearLayoutManager(activity)
        binding.localNewsList.setHasFixedSize(true)
        viewLifecycleOwner.lifecycleScope.launch {
            binding.localNewsList.adapter = ArticleAdapter(model.getArticle(newId))
        }
        return binding.root
    }
}