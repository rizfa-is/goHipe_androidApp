package com.istekno.gohipeandroidapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentMainScreenBinding
import kotlinx.android.synthetic.main.fragment_main_screen.*

class MainScreenFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_mainfrg_engineer.setOnClickListener(this)
        btn_mainfrg_company.setOnClickListener(this)
    }
    
    override fun onClick(v: View) {
        val mFragmentManager = fragmentManager
        var mFragment : Fragment
        when (v.id) {
            R.id.btn_mainfrg_engineer -> {
                mFragment = LoginScreenFragment()
                mFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.frame_container, mFragment, MainScreenFragment::class.java.simpleName)
                    commit()
                }
            }
            R.id.btn_mainfrg_company -> {
                mFragment = LoginScreenFragment()
                mFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.frame_container, mFragment, MainScreenFragment::class.java.simpleName)
                    commit()
                }
            }
        }
    }
}