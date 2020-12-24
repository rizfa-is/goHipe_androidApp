package com.istekno.gohipeandroidapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ItemListSearchEngineerBinding
import com.istekno.gohipeandroidapp.models.User

class ListSearchEngineerAdapter(private val listUser: List<User>, private val onItemClickCallback: OnItemClickCallback): RecyclerView.Adapter<ListSearchEngineerAdapter.ListViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }

    inner class ListViewHolder(val binding: ItemListSearchEngineerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.image)
                .apply(RequestOptions().override(150, 150))
                .into(binding.imgListSearchEng)

            binding.tvListSearchEngName.text = user.name
            binding.tvListSearchEngJob.text = user.job

            chipViewInit(user.ability, itemView, binding)

            this.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[this.adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        return ListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_list_search_engineer, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    @SuppressLint("SetTextI18n")
    private fun chipViewInit(listAbility: List<String>, view: View, binding: ItemListSearchEngineerBinding) {
        for (element in listAbility) {
            val chip = Chip(view.context)

            if (listAbility.indexOf(element) <= 2) {
                chip.width = 17
                chip.height = 7
                chip.chipCornerRadius = 20F
                chip.chipBackgroundColor = view.resources.getColorStateList(R.color.theme_orange)
                chip.text = element
                chip.textSize = 12F
                chip.setTextColor(view.resources.getColor(R.color.white))

                binding.cgSearchengAbility.addView(chip)
            } else if (listAbility.indexOf(element) == listAbility.size - 1) {
                chip.chipBackgroundColor = view.resources.getColorStateList(R.color.white)
                chip.text = "${listAbility.size - 3}+"
                chip.textSize = 14F
                binding.cgSearchengAbility.addView(chip)
            }
        }
    }
}