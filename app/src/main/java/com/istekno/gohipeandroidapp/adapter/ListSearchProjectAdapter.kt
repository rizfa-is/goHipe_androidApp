package com.istekno.gohipeandroidapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ItemListSearchProjectBinding
import com.istekno.gohipeandroidapp.retrofit.ProjectModelResponse

class ListSearchProjectAdapter(private val level: Int): RecyclerView.Adapter<ListSearchProjectAdapter.ListViewHolder>() {

    companion object {
        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listUser = mutableListOf<ProjectModelResponse>()

    fun setData(projectModelResponse: List<ProjectModelResponse>) {
        listUser.clear()
        listUser.addAll(projectModelResponse)
        notifyDataSetChanged()
    }

    fun onItemClickCallbak(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(projectModelResponse: ProjectModelResponse)
        fun onDeleteClicked(projectModelResponse: ProjectModelResponse)
    }

    inner class ListViewHolder(val binding: ItemListSearchProjectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(projectModelResponse: ProjectModelResponse) {
            binding.model = projectModelResponse
            Glide.with(itemView.context)
                .load(imageLink + projectModelResponse.pjImage)
                .apply(RequestOptions().override(150, 150))
                .into(binding.imgListSearchProject)

            if (level == 1) binding.imgDelete.visibility = View.VISIBLE

            binding.imgDelete.setOnClickListener { onItemClickCallback.onDeleteClicked(listUser[this.adapterPosition]) }
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