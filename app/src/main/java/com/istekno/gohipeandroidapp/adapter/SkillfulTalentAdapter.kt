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
import com.istekno.gohipeandroidapp.databinding.ItemSkillfulTalentBinding
import com.istekno.gohipeandroidapp.models.MostPopular
import com.istekno.gohipeandroidapp.models.User

class SkillfulTalentAdapter: RecyclerView.Adapter<SkillfulTalentAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listUser =  ArrayList<User>()

    fun setData(list: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnitemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }

    inner class ListViewHolder(val binding: ItemSkillfulTalentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.modelSkillful = user
            Glide.with(itemView.context)
                .load(user.image)
                .apply(RequestOptions().override(150, 150))
                .into(binding.imgSkillfulTalentEng)

            chipViewInit(user.ability, itemView, binding)

            this.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[this.adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        return ListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_skillful_talent, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    @SuppressLint("SetTextI18n")
    private fun chipViewInit(listAbility: List<String>, view: View, binding: ItemSkillfulTalentBinding) {
        for (element in listAbility) {
            val chip = Chip(view.context)

            if (listAbility.indexOf(element) <= 2) {
                chip.width = 17
                chip.height = 7
                chip.chipCornerRadius = 20F
                chip.chipBackgroundColor = view.resources.getColorStateList(R.color.white)
                chip.text = element
                chip.textSize = 12F
                chip.setTextColor(view.resources.getColor(R.color.theme_green))

                binding.cgSearchengAbility.addView(chip)
            } else if (listAbility.indexOf(element) == listAbility.size - 1) {
                chip.chipBackgroundColor = view.resources.getColorStateList(R.color.theme_green)
                chip.text = "${listAbility.size - 3}+"
                chip.textSize = 14F
                chip.setTextColor(view.resources.getColor(R.color.white))
                binding.cgSearchengAbility.addView(chip)
            }
        }
    }
}
