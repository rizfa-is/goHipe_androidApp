package com.istekno.gohipeandroidapp.ui.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentResetPasswordScreenBinding
import com.istekno.gohipeandroidapp.ui.login.LoginScreenFragment

class ResetPasswordScreenFragment : Fragment() {

    private lateinit var  binding: FragmentResetPasswordScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnResetpassfrgSendmail.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, LoginScreenFragment())?.commit()
        }
    }

}