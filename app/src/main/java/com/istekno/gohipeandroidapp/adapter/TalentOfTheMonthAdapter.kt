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
import com.istekno.gohipeandroidapp.retrofit.AbilityModel
import com.istekno.gohipeandroidapp.retrofit.EngineerModelRequest
import com.istekno.gohipeandroidapp.retrofit.ExperienceModel
import com.istekno.gohipeandroidapp.retrofit.PortfolioModel

class TalentOfTheMonthAdapter: RecyclerView.Adapter<TalentOfTheMonthAdapter.ListViewHolder>() {

    companion object {
        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listEngineer = mutableListOf<EngineerModelRequest>()

    fun setData(listEn: List<EngineerModelRequest>) {
        listEngineer.clear()
        listEngineer.addAll(listEn)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(engineerModelRequest: EngineerModelRequest)
    }

    inner class ListViewHolder(val binding: ItemListMostPopularBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(engineerModelRequest: EngineerModelRequest) {
            binding.modelMostPop = engineerModelRequest
            Glide.with(itemView.context)
                    .load(imageLink + engineerModelRequest.enAvatar)
                    .apply(RequestOptions().override(150, 150))
                    .into(binding.imgMostPopular)

            this.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listEngineer[this.adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        return ListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_list_most_popular, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listEngineer[position])
    }

    override fun getItemCount(): Int = listEngineer.size
}