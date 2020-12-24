package com.istekno.gohipeandroidapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ItemListScouterOfTheMonthBinding
import com.istekno.gohipeandroidapp.models.ScouterTop

class ScouterOfTheMonthAdapter(private val listUser: ArrayList<ScouterTop>, private val onItemClickCallback: OnItemClickCallback): RecyclerView.Adapter<ScouterOfTheMonthAdapter.ListViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(scouterTop: ScouterTop)
    }

    inner class ListViewHolder(val binding: ItemListScouterOfTheMonthBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(scouterTop: ScouterTop) {
            Glide.with(itemView.context)
                .load(scouterTop.image)
                .apply(RequestOptions().override(150, 150))
                .into(binding.imgSkillful)

            binding.tvSkillfulName.text = scouterTop.name
            binding.tvSkillfulField.text = scouterTop.field
            binding.tvSkillfulProject.text = scouterTop.project.toString()
            binding.tvSkillfulEngineerhired.text = "${scouterTop.engineer_hired}"
            binding.tvSkillfulReqrate.text = "${scouterTop.requirement_rate}"

            this.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[this.adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        return ListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_list_scouter_of_the_month, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size
}