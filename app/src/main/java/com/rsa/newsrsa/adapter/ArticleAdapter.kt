package com.rsa.newsrsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rsa.newsrsa.R
import com.rsa.newsrsa.api.response_handler.NewsData.Article
import com.rsa.newsrsa.databinding.RowListItemForMoreBinding
import javax.inject.Inject

class ArticleAdapter(private val mList : List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    private lateinit var binding: RowListItemForMoreBinding
    inner class ArticleViewHolder(binding: RowListItemForMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var article: Article? = null
        fun bind(item: Article) {
            article = item
            showData(article!!)
        }
    }

    private fun showData(article: Article) {

        binding.headline.text = article.description
        binding.title.text = article.title
        binding.forMore.text = article.url
        binding.source.text = article.author
        Glide.with(binding.root.context).load(article.urlToImage).placeholder(R.mipmap.news_icon)
            .into(binding.image)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = mList[position]
        if (article != null) {
            holder.bind(article)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        binding = RowListItemForMoreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}