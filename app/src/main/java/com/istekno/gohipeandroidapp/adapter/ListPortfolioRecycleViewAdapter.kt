package com.istekno.gohipeandroidapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ItemRowPortfolioBinding
import com.istekno.gohipeandroidapp.models.Portfolio

class ListPortfolioRecycleViewAdapter(private val listPortfolio: ArrayList<Portfolio>) : RecyclerView.Adapter<ListPortfolioRecycleViewAdapter.ListViewHolder>() {

    inner class ListViewHolder(val binding: ItemRowPortfolioBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(portfolio: Portfolio) {
            Glide.with(itemView.context)
                .load(portfolio.image)
                .apply(RequestOptions().override(900, 400))
                .into(binding.imgItemRowPortfolio)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        return ListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_row_portfolio, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listPortfolio[position])
    }

    override fun getItemCount(): Int = listPortfolio.size
}