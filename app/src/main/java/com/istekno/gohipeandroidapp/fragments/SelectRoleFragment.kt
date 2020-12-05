package com.istekno.gohipeandroidapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.istekno.gohipeandroidapp.R
import kotlinx.android.synthetic.main.fragment_select_role.*

class SelectRoleFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_role, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_rolefrg_engineer.setOnClickListener(this)
        tv_rolefrg_engineer.setOnClickListener(this)
        img_rolefrg_company.setOnClickListener(this)
        tv_rolefrg_company.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val mFragmentManager = fragmentManager
        val mFragment : Fragment

        when (v.id) {
            R.id.img_rolefrg_engineer -> {
                mFragment = EngineerRegisterScreenFragment()
                mFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.frame_container, mFragment, EngineerRegisterScreenFragment::class.java.simpleName)
                    commit()
                }
            }
            R.id.tv_rolefrg_engineer -> {
                mFragment = EngineerRegisterScreenFragment()
                mFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.frame_container, mFragment, EngineerRegisterScreenFragment::class.java.simpleName)
                    commit()
                }
            }
        }
    }
}