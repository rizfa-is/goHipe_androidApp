package com.istekno.gohipeandroidapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentSelectRoleBinding
import com.istekno.gohipeandroidapp.ui.company.register.CompanyRegisterScreenFragment
import com.istekno.gohipeandroidapp.ui.engineer.register.EngineerRegisterScreenFragment

class SelectRoleFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentSelectRoleBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_role, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgRolefrgEngineer.setOnClickListener(this)
        binding.tvRolefrgEngineer.setOnClickListener(this)
        binding.imgRolefrgCompany.setOnClickListener(this)
        binding.tvRolefrgCompany.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_rolefrg_engineer -> {
                fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, EngineerRegisterScreenFragment())?.addToBackStack(null)?.commit()
            }
            R.id.tv_rolefrg_engineer -> {
                fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, EngineerRegisterScreenFragment())?.addToBackStack(null)?.commit()
            }
            R.id.img_rolefrg_company -> {
                fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, CompanyRegisterScreenFragment())?.addToBackStack(null)?.commit()
            }
            R.id.tv_rolefrg_company -> {
                fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, CompanyRegisterScreenFragment())?.addToBackStack(null)?.commit()
            }
        }
    }
}