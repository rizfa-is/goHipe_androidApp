package com.istekno.gohipeandroidapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ItemListMostPopularBinding
import com.istekno.gohipeandroidapp.models.MostPopular

class TalentOfTheMonthAdapter(private val listUser: ArrayList<MostPopular>, private val onItemClickCallback: OnItemClickCallback): RecyclerView.Adapter<TalentOfTheMonthAdapter.ListViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(mostPopular: MostPopular)
    }

    inner class ListViewHolder(val binding: ItemListMostPopularBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(mostPopular: MostPopular) {
            Glide.with(itemView.context)
                    .load(mostPopular.image)
                    .apply(RequestOptions().override(150, 150))
                    .into(binding.imgMostPopular)

            binding.tvMostPopularName.text = mostPopular.name
            binding.tvMostPopularJob.text = mostPopular.job
            binding.tvMostPopularProject.text = mostPopular.project.toString()
            binding.tvMostPopularDevliverytime.text = "${mostPopular.delivery_time} days"
            binding.tvMostPopularProjectconvrate.text = "${mostPopular.conv_rate} %"

            this.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[this.adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        return ListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_list_most_popular, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size
}