package com.istekno.gohipeandroidapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ItemListMostPopularBinding
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.EngineerModelResponse

class TalentOfTheMonthAdapter: RecyclerView.Adapter<TalentOfTheMonthAdapter.ListViewHolder>() {

    companion object {
        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listEngineer = mutableListOf<EngineerModelResponse>()

    fun setData(listEn: List<EngineerModelResponse>) {
        listEngineer.clear()
        listEngineer.addAll(listEn)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(engineerModelResponse: EngineerModelResponse)
    }

    inner class ListViewHolder(val binding: ItemListMostPopularBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(engineerModelResponse: EngineerModelResponse) {
            binding.modelMostPop = engineerModelResponse
            Glide.with(itemView.context)
                    .load(imageLink + engineerModelResponse.enAvatar)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error))
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