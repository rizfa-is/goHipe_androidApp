package com.istekno.gohipeandroidapp.maincontent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentForgotPasswordScreenBinding

class ForgotPasswordScreenFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnForgotpassfrgSendmail.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, ResetPasswordScreenFragment())?.addToBackStack(null)?.commit()
        }
    }
}