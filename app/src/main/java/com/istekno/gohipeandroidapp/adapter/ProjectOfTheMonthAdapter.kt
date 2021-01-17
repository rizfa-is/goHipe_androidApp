package com.istekno.gohipeandroidapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ItemProjectOfTheMonthBinding
import com.istekno.gohipeandroidapp.retrofit.HireModelResponse
import com.istekno.gohipeandroidapp.retrofit.ProjectModelResponse

class ProjectOfTheMonthAdapter: RecyclerView.Adapter<ProjectOfTheMonthAdapter.ListViewHolder>() {

    companion object {
        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listProject = mutableListOf<HireModelResponse>()

    fun setData(listEn: List<HireModelResponse>) {
        listProject.clear()
        listProject.addAll(listEn)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(hireModelResponse: HireModelResponse)
    }

    inner class ListViewHolder(val binding: ItemProjectOfTheMonthBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(hireModelResponse: HireModelResponse) {
            binding.model = hireModelResponse
            Glide.with(itemView.context)
                .load(imageLink +  hireModelResponse.pjImage)
                .apply(RequestOptions().override(180, 125))
                .into(binding.imgProjectOfTheMonth)

            this.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listProject[this.adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        return ListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_project_of_the_month, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listProject[position])
    }

    override fun getItemCount(): Int = listProject.size
}