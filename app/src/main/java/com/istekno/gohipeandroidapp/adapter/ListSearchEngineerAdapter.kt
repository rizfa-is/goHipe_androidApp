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
import com.istekno.gohipeandroidapp.retrofit.*

class ListSearchEngineerAdapter: RecyclerView.Adapter<ListSearchEngineerAdapter.ListViewHolder>() {

    companion object {
        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listEngineer = mutableListOf<EngineerModelResponse>()
    private val listAbility = mutableListOf<AbilityM>()

    fun setData(listEn: List<EngineerModelResponse>, listAb: List<AbilityM>) {
        listEngineer.clear()
        listEngineer.addAll(listEn)
        listAbility.clear()
        listAbility.addAll(listAb)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(engineerModelResponse: EngineerModelResponse)
    }

    inner class ListViewHolder(val binding: ItemListSearchEngineerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(engineerModelResponse: EngineerModelResponse, abilityM: AbilityM) {
            binding.model = engineerModelResponse
            Glide.with(itemView.context)
                .load(imageLink + engineerModelResponse.enAvatar)
                .apply(RequestOptions().override(150, 150))
                .into(binding.imgListSearchEng)

            chipViewInit(abilityM.list, itemView, binding)

            this.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listEngineer[this.adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        return ListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_list_search_engineer, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listEngineer[position], listAbility[position])
    }

    override fun getItemCount(): Int = listEngineer.size

    @SuppressLint("SetTextI18n")
    private fun chipViewInit(listAbility: List<Ability>, view: View, binding: ItemListSearchEngineerBinding) {
        for (i in listAbility.indices) {
            val chip = Chip(view.context)
            val abName = listAbility[i].abName

            if (i <= 2) {
                chip.width = 17
                chip.height = 7
                chip.chipCornerRadius = 20F
                chip.chipBackgroundColor = view.resources.getColorStateList(R.color.theme_orange)
                chip.text = abName
                chip.textSize = 12F
                chip.setTextColor(view.resources.getColor(R.color.white))

                binding.cgSearchengAbility.addView(chip)
            } else if (i == listAbility.size - 1) {
                chip.chipBackgroundColor = view.resources.getColorStateList(R.color.white)
                chip.text = "${listAbility.size - 3}+"
                chip.textSize = 14F
                binding.cgSearchengAbility.addView(chip)
            }
        }
    }
}