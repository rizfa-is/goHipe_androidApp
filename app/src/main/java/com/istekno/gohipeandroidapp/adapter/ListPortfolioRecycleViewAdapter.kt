package com.istekno.gohipeandroidapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ItemRowPortfolioBinding
import com.istekno.gohipeandroidapp.retrofit.ExperienceModel
import com.istekno.gohipeandroidapp.retrofit.PortfolioModel

class ListPortfolioRecycleViewAdapter(private val type: Int) : RecyclerView.Adapter<ListPortfolioRecycleViewAdapter.ListViewHolder>() {

    companion object {
        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listPortfolio = mutableListOf<PortfolioModel>()

    fun setData(listPr: List<PortfolioModel>) {
        listPortfolio.clear()
        listPortfolio.addAll(listPr)
        notifyDataSetChanged()
    }

    fun onItemClickCallbak(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onUpdatePressed(portfolioModel: PortfolioModel)
        fun onDeletePressed(portfolioModel: PortfolioModel)
    }

    inner class ListViewHolder(val binding: ItemRowPortfolioBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(portfolioModel: PortfolioModel) {
            Glide.with(itemView.context)
                .load(imageLink + portfolioModel.prImg)
                .apply(RequestOptions().override(200, 100))
                .into(binding.imgItemRowPortfolio)

            if (type == 1) binding.llEditporto.visibility = View.GONE
            binding.btnEditportoEdit.setOnClickListener { onItemClickCallback.onUpdatePressed(listPortfolio[this.adapterPosition]) }
            binding.btnEditportoDelete.setOnClickListener { onItemClickCallback.onDeletePressed(listPortfolio[this.adapterPosition]) }
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