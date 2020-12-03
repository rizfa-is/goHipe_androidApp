package com.istekno.gohipeandroidapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.MainScreenActivity
import kotlinx.android.synthetic.main.fragment_login_screen.*

class LoginScreenFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_login_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mFragmentManager = fragmentManager
        var mFragment : Fragment

        tv_loginfrg_register_here.setOnClickListener {
            mFragment = RegisterScreenFragment()
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container, mFragment, RegisterScreenFragment::class.java.simpleName)
                commit()
            }
        }
        img_loginfrg_back.setOnClickListener {
            mFragment = MainScreenFragment()
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container, mFragment, MainScreenFragment::class.java.simpleName)
                commit()
            }
        }
    }
}