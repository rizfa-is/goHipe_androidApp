package com.istekno.gohipeandroidapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ItemRowExperienceBinding
import com.istekno.gohipeandroidapp.models.Experience
import kotlin.collections.ArrayList

class ListExperienceRecycleViewAdapter(private val listExperience: ArrayList<Experience>) : RecyclerView.Adapter<ListExperienceRecycleViewAdapter.ListViewHolder>() {

    inner class ListViewHolder(val binding: ItemRowExperienceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(experience: Experience) {
            Glide.with(itemView.context)
                .load(experience.image)
                .apply(RequestOptions().override(150, 150))
                .into(binding.imgItemRowExperience)

            binding.tvItemRowExperienceJob.text = experience.job
            binding.tvItemRowExperienceCompany.text = experience.company
            binding.tvItemRowExperienceStartenddate.text = experience.period
            binding.tvItemRowExperienceTotalmonths.text = experience.totalMonth
            binding.tvItemRowExperienceDesc.text = experience.description
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        return ListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_row_experience, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listExperience[position])
    }

    override fun getItemCount(): Int = listExperience.size
}