package com.istekno.gohipeandroidapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import kotlinx.android.synthetic.main.fragment_engineer_register_screen.*
import kotlinx.android.synthetic.main.fragment_forgot_password_screen.*
import kotlinx.android.synthetic.main.fragment_reset_password_screen.*

class ResetPasswordScreenFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mFragmentManager = fragmentManager
        var mFragment : Fragment

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mFragment = ForgotPasswordScreenFragment()
                mFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.frame_container_logregact, mFragment, ForgotPasswordScreenFragment::class.java.simpleName)
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
        return inflater.inflate(R.layout.fragment_reset_password_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mFragmentManager = fragmentManager
        var mFragment : Fragment

        btn_resetpassfrg_sendmail.setOnClickListener {
            mFragment = LoginScreenFragment()
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container_logregact, mFragment, LoginScreenFragment::class.java.simpleName)
                commit()
            }
        }
    }

}