package com.istekno.gohipeandroidapp.fragments.engineer

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerDetailHireScreenBinding

class EngineerDetailHireScreenFragment(private val hireStatus: Int?) : Fragment() {

    companion object {
        private const val APPROVED = "APPROVED"
        private const val REJECTED = "REJECTED"
    }

    private lateinit var binding: FragmentEngineerDetailHireScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_detail_hire_screen, container, false)

        detailStatus(hireStatus)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEngdetailhirefrgEditproject.setOnClickListener {
            //Mengubah status on waiting ke approved
        }

        binding.btnEngdetailhirefrgEditproject2.setOnClickListener {
            //Mengubah status on waiting ke rejected
        }
    }

    private fun detailStatus(hireStatus: Int?) {
        when (hireStatus) {
            0 -> {
                binding.tvEngdetailhirefrghireStatus.visibility = View.GONE
            }
            1 -> {
                binding.btnEngdetailhirefrgEditproject.visibility = View.GONE
                binding.btnEngdetailhirefrgEditproject2.visibility = View.GONE
                binding.tvEngdetailhirefrghireStatus.text = APPROVED
            }
            2 -> {
                binding.btnEngdetailhirefrgEditproject.visibility = View.GONE
                binding.btnEngdetailhirefrgEditproject2.visibility = View.GONE
                binding.tvEngdetailhirefrghireStatus.text = REJECTED
                binding.tvEngdetailhirefrghireStatus.setTextColor(Color.RED)
            }
        }
    }
}