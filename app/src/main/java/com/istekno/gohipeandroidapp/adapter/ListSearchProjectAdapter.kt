package com.istekno.gohipeandroidapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ItemListSearchProjectBinding
import com.istekno.gohipeandroidapp.models.SearchProject

class ListSearchProjectAdapter(private val listUser: List<SearchProject>, private val onItemClickCallback: OnItemClickCallback): RecyclerView.Adapter<ListSearchProjectAdapter.ListViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(searchProject: SearchProject)
    }

    inner class ListViewHolder(val binding: ItemListSearchProjectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchProject: SearchProject) {
            Glide.with(itemView.context)
                .load(searchProject.image)
                .apply(RequestOptions().override(150, 150))
                .into(binding.imgListSearchProject)

            binding.tvListSearchProjectName.text = searchProject.name
            binding.tvListSearchProjectDesc.text = searchProject.desc

            this.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[this.adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        return ListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_list_search_project, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size
}