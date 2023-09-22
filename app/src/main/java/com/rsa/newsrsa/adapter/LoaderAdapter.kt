package com.rsa.newsrsa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rsa.newsrsa.databinding.LoaderItemBinding

class LoaderAdapter : LoadStateAdapter<LoaderAdapter.LoaderViewHolder>() {
    private lateinit var binding : LoaderItemBinding
    inner class LoaderViewHolder(itemView: LoaderItemBinding) : RecyclerView.ViewHolder(itemView.root)
    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        when(loadState){
            LoadState.Loading->{
                binding.progressBar.visibility= View.VISIBLE
                binding.txtMsg.visibility = View.GONE
            }
//                LoadState.Error->{
//                    binding.progressBar.visibility= View.GONE
//                    binding.txtMsg.visibility = View.VISIBLE
//                }
            else -> {
                binding.progressBar.visibility= View.GONE
                binding.txtMsg.visibility = View.GONE
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        binding = LoaderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoaderViewHolder(binding)
    }
}