package com.istekno.gohipeandroidapp.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.models.User

class UserDetailProfileAdapter(private val listUser: ArrayList<User>, private val onItemClickCallback: OnItemClickCallback): RecyclerView.Adapter<UserDetailProfileAdapter.ListViewHolder>() {

    private val listAbility = GoHipeDatabases.ability

    interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name : TextView = itemView.findViewById(R.id.tv_enprofifrg_name)
        private val job : TextView = itemView.findViewById(R.id.tv_enprofifrg_job)
        private val image : ImageView = itemView.findViewById(R.id.img_enprofifrg_avatar)
        private val chipGroup: ChipGroup = itemView.findViewById(R.id.cg_enprofifrg_developer)

        fun bind(user: User) {
            Glide.with(itemView.context)
                    .load(user.image)
                    .apply(RequestOptions().override(150, 150))
                    .into(image)

            name.text = user.name
            job.text = user.job
            chipViewInit(itemView, chipGroup)

            this.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[this.adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_list_developer, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    @SuppressLint("ResourceAsColor")
    private fun chipViewInit(view: View, chipGroup: ChipGroup) {
        for (i in 0 until listAbility.size) {
            val chip = Chip(view.context)
            chip.chipCornerRadius = 30F
            chip.chipBackgroundColor = ColorStateList.valueOf(R.color.theme_orange)
            chip.text = listAbility[i].toString()
            chip.setTextColor(Color.WHITE)

            chipGroup.addView(chip)
        }
    }
}