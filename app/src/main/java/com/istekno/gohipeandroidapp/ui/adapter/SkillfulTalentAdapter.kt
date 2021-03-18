package com.istekno.gohipeandroidapp.ui.adapter

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
import com.istekno.gohipeandroidapp.databinding.ItemSkillfulTalentBinding
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.Ability
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.AbilityM
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.EngineerModelResponse

class SkillfulTalentAdapter: RecyclerView.Adapter<SkillfulTalentAdapter.ListViewHolder>() {

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

    inner class ListViewHolder(val binding: ItemSkillfulTalentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(engineerModelResponse: EngineerModelResponse, abilityM: AbilityM) {
            binding.modelSkillful = engineerModelResponse
            Glide.with(itemView.context)
                .load(imageLink + engineerModelResponse.enAvatar)
                    .placeholder(R.drawable.ic_avatar_en)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error))
                .into(binding.imgSkillfulTalentEng)

            chipViewInit(abilityM.list, itemView, binding)

            this.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listEngineer[this.adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        return ListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_skillful_talent, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listEngineer[position], listAbility[position])
    }

    override fun getItemCount(): Int = listEngineer.size

    @SuppressLint("SetTextI18n")
    private fun chipViewInit(listAbility: List<Ability>, view: View, binding: ItemSkillfulTalentBinding) {
        binding.cgSearchengAbility.removeAllViews()

        for (i in listAbility.indices) {
            val chip = Chip(view.context)
            val abName = listAbility[i].abName

            if (i <= 2) {
                chip.width = 17
                chip.height = 7
                chip.chipCornerRadius = 20F
                chip.chipBackgroundColor = view.resources.getColorStateList(R.color.theme_green)
                chip.text = abName
                chip.textSize = 12F
                chip.setTextColor(view.resources.getColor(R.color.white))

                binding.cgSearchengAbility.addView(chip)
            } else if (i == listAbility.size - 1) {
                chip.chipBackgroundColor = view.resources.getColorStateList(R.color.white)
                chip.text = "${listAbility.size - 3}+"
                chip.textSize = 14F
                chip.setTextColor(view.resources.getColor(R.color.theme_green))
                binding.cgSearchengAbility.addView(chip)
            }
        }
    }
}
