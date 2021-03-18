package com.istekno.gohipeandroidapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ItemListHireBinding
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.HireModelResponse

class ListHireAdapter(private val hireStatus: Int) : RecyclerView.Adapter<ListHireAdapter.ListViewHolder>() {

    companion object {
        const val STATUS_WAIT = "On wait"
        const val STATUS_PROGRESS = "On progress"
        const val STATUS_FINISHED = "Finished"

        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listHire = mutableListOf<HireModelResponse>()

    fun setData(listHr: List<HireModelResponse>) {
        listHire.clear()
        listHire.addAll(listHr)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(hireModelResponse: HireModelResponse)
    }

    inner class ListViewHolder(val binding: ItemListHireBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hireModelResponse: HireModelResponse) {
            binding.model = hireModelResponse
            Glide.with(itemView.context)
                .load(imageLink + hireModelResponse.pjImage)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_error))
                .into(binding.imgListSearchProject)

            binding.tvListSearchProjectDeadline.text = hireModelResponse.pjDeadline?.split('T')?.get(0) ?: "null"

            hireStatusMode(binding, itemView)

            this.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listHire[this.adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_list_hire, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listHire[position])
    }

    override fun getItemCount(): Int = listHire.size

    private fun hireStatusMode(binding: ItemListHireBinding, view: View) {

        val statusSign = binding.tvListSearchProjectStatus
        val bgStatusSign = binding.imgListSearchProjectBgstatus

        when (hireStatus) {
            0 -> {
                statusSign.text = STATUS_WAIT
                statusSign.setTextColor(view.resources.getColor(R.color.theme_orange))
                bgStatusSign.background = view.resources.getDrawable(R.color.theme_orange_trans)
            }
            1 -> {
                statusSign.text = STATUS_PROGRESS
                statusSign.setTextColor(view.resources.getColor(R.color.theme_orange))
                bgStatusSign.background = view.resources.getDrawable(R.color.theme_orange_trans)
            }
            2 -> {
                statusSign.text = STATUS_FINISHED
                statusSign.setTextColor(view.resources.getColor(R.color.theme_green))
                bgStatusSign.background = view.resources.getDrawable(R.color.theme_green_trans)
            }
        }
    }

}