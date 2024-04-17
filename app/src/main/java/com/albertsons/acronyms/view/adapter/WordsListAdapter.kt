package com.albertsons.acronyms.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albertsons.acronyms.databinding.RowItemBinding
import com.albertsons.acronyms.model.AcronymWords
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class WordsListAdapter @Inject constructor() : RecyclerView.Adapter<MainViewHolder>() {

    private var resultList = mutableListOf<AcronymWords>()

    fun setList(lfs: List<AcronymWords>) {
        this.resultList = lfs.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowItemBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val result = resultList[position]
        holder.binding.result.text = result.word
    }

    override fun getItemCount(): Int {
        return resultList.size
    }
}

/**
 * ViewHolder for List
 */
class MainViewHolder(val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root)