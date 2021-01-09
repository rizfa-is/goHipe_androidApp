package com.istekno.gohipeandroidapp.adapter

import android.annotation.SuppressLint
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
import com.istekno.gohipeandroidapp.retrofit.ExperienceModel
import com.istekno.gohipeandroidapp.retrofit.PortfolioModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.collections.ArrayList

class ListExperienceRecycleViewAdapter(private val type: Int): RecyclerView.Adapter<ListExperienceRecycleViewAdapter.ListViewHolder>() {

    val listImageEx = listOf(
            "https://assets.tokopedia.net/assets-tokopedia-lite/v2/arael/kratos/36c1015e.png",
            "https://upload.wikimedia.org/wikipedia/commons/b/b5/Shopee-logo.jpg",
            "https://blog.hubspot.com/hubfs/image8-2.jpg",
            "https://download.logo.wine/logo/Tesla%2C_Inc./Tesla%2C_Inc.-Logo.wine.png",
            "https://1.bp.blogspot.com/-LgTa-xDiknI/X4EflN56boI/AAAAAAAAPuk/24YyKnqiGkwRS9-_9suPKkfsAwO4wHYEgCLcBGAsYHQ/s0/image9.png"
    )

    private val listExperience = mutableListOf<ExperienceModel>()

    fun setData(listEx: List<ExperienceModel>) {
        listExperience.clear()
        listExperience.addAll(listEx)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(val binding: ItemRowExperienceBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(experienceModel: ExperienceModel) {
            val startDate = experienceModel.exStartDate!!.split('T')[0]
            val endDate = experienceModel.exEndDate!!.split('T')[0]
            val sum = ChronoUnit.MONTHS.between(LocalDate.parse(startDate).withDayOfMonth(1), LocalDate.parse(endDate).withDayOfMonth(1))

            binding.model = experienceModel
            Glide.with(itemView.context)
                .load(listImageEx.random())
                .apply(RequestOptions().override(150, 150))
                .into(binding.imgItemRowExperience)

            binding.tvItemRowExperienceStartenddate.text = "$startDate - $endDate"
            binding.tvItemRowExperienceTotalmonths.text = "$sum months"

            if (type == 1) binding.llEditexperi.visibility = View.GONE
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