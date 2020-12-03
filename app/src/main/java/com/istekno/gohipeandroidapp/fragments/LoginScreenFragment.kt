package com.istekno.gohipeandroidapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.istekno.gohipeandroidapp.R
import kotlinx.android.synthetic.main.fragment_reset_password_screen.*

class LoginScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_register_here.setOnClickListener {
            val mFragmentManager = fragmentManager
            var mFragment = RegisterScreenFragment()
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container, mFragment, RegisterScreenFragment::class.java.simpleName)
                commit()
            }
        }
    }
}