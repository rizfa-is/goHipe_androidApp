package com.istekno.gohipeandroidapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ItemRowPortfolioBinding
import com.istekno.gohipeandroidapp.retrofit.PortfolioModel

class ListPortfolioRecycleViewAdapter : RecyclerView.Adapter<ListPortfolioRecycleViewAdapter.ListViewHolder>() {

    companion object {
        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private val listPortfolio = mutableListOf<PortfolioModel>()

    fun setData(listPr: List<PortfolioModel>) {
        listPortfolio.clear()
        listPortfolio.addAll(listPr)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(val binding: ItemRowPortfolioBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(portfolioModel: PortfolioModel) {
            Glide.with(itemView.context)
                .load(imageLink + portfolioModel.prImg)
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