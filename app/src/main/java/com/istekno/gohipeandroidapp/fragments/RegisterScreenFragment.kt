package com.istekno.gohipeandroidapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.istekno.gohipeandroidapp.R
import kotlinx.android.synthetic.main.fragment_register_screen.*


class RegisterScreenFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mFragmentManager = fragmentManager
        var mFragment : Fragment

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mFragment = MainScreenFragment()
                mFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.frame_container, mFragment, MainScreenFragment::class.java.simpleName)
                    commit()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mFragmentManager = fragmentManager
        var mFragment : Fragment

        tv_registerfrg_login_here.setOnClickListener {
            mFragment = LoginScreenFragment()
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container, mFragment, LoginScreenFragment::class.java.simpleName)
                commit()
            }
        }
        img_registerfrg_back.setOnClickListener {
            mFragment = MainScreenFragment()
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container, mFragment, MainScreenFragment::class.java.simpleName)
                commit()
            }
        }
    }
}