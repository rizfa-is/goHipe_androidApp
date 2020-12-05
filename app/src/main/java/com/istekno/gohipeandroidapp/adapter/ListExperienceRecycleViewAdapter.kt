package com.istekno.gohipeandroidapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.data.Experience
import kotlinx.android.synthetic.main.item_row_experience.view.*
import kotlin.collections.ArrayList

class ListExperienceRecycleViewAdapter(private val listExperience: ArrayList<Experience>) : RecyclerView.Adapter<ListExperienceRecycleViewAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val job : TextView = itemView.findViewById(R.id.tv_item_row_experience_job)
        private val company : TextView = itemView.findViewById(R.id.tv_item_row_experience_company)
        private val period : TextView = itemView.findViewById(R.id.tv_item_row_experience_startenddate)
        private val totalMonth : TextView = itemView.findViewById(R.id.tv_item_row_experience_totalmonths)
        private val desc : TextView = itemView.findViewById(R.id.tv_item_row_experience_desc)

        fun bind(experience: Experience) {
            Glide.with(itemView.context)
                .load(experience.image)
                .apply(RequestOptions().override(150, 150))
                .into(itemView.img_item_row_experience)

            job.text = experience.job
            company.text = experience.company
            period.text = experience.period
            totalMonth.text = experience.totalMonth
            desc.text = experience.description
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_experience, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listExperience[position])
    }

    override fun getItemCount(): Int = listExperience.size
}