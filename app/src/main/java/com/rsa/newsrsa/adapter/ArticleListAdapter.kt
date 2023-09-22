package com.rsa.newsrsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rsa.newsrsa.R
import com.rsa.newsrsa.api.response_handler.NewsData.Article
import com.rsa.newsrsa.data.repository.NewsLocalRepository
import com.rsa.newsrsa.databinding.RowListItemBinding
import com.rsa.newsrsa.ui.news.TabFragmentDirections
import com.rsa.newsrsa.ui.readlater.NewsReadLaterFragmentDirections
import com.rsa.newsrsa.utils.ResourcesProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArticleListAdapter @Inject constructor(
    private val newsLocalRepository: NewsLocalRepository,
    private val resourcesProvider: ResourcesProvider,
    private var findNavController: NavController
) : PagingDataAdapter<Article, ArticleListAdapter.ArticleViewHolder>(ARTICLE_COMPARATOR) {
    private lateinit var binding: RowListItemBinding
    private var isRead : Boolean = false
    private var readLater : Boolean = false
    inner class ArticleViewHolder(binding: RowListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var article: Article? = null
        fun bind(item: Article) {
            article = item
            showData(article!!)
        }
    }

    private fun showData(article: Article) {

        binding.headline.text = article.title
        if(article.isRead)
            binding.imgRead.setImageDrawable(resourcesProvider.getDrawable(R.drawable.baseline_flag_read_24))
        else
            binding.imgRead.setImageDrawable(resourcesProvider.getDrawable(R.drawable.baseline_flag_24))
        Glide.with(binding.root.context).load(article.urlToImage).placeholder(R.mipmap.news_icon)
            .into(binding.image)
        binding.imgDelete.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                article.id.let { it1 -> newsLocalRepository.deleteNewsArticle(it1) }
                article.id.let { it1 -> newsLocalRepository.deleteArticleRemoteKey(it1) }
                notifyDataSetChanged()
            }
        }
        binding.imgRead.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                isRead = !newsLocalRepository.isArticleMarkRead(article.id)
                article.id.let { it1 -> newsLocalRepository.isReadArticle(isRead,it1) }
            }
            if(isRead)
                binding.imgRead.setImageDrawable(resourcesProvider.getDrawable(R.drawable.baseline_flag_read_24))
            else
                binding.imgRead.setImageDrawable(resourcesProvider.getDrawable(R.drawable.baseline_flag_24))
            notifyDataSetChanged()
        }
        binding.imgReadLater.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                readLater = !newsLocalRepository.isArticleMarkReadLater(article.id)
                article.id.let { it1 -> newsLocalRepository.readLaterArticle(readLater,it1) }
                notifyDataSetChanged()
            }
        }
        binding.rootView.setOnClickListener {

            val action = TabFragmentDirections.actionTabFragmentToReadmoreFragment(article.id)
            findNavController.navigate(action)
        }
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.row_list_item, parent, false
        )
        return ArticleViewHolder(binding)
    }

    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.url == newItem.url

            override fun areContentsTheSame(
                oldItem: Article, newItem: Article
            ): Boolean = oldItem == newItem
        }
    }
}