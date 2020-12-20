package com.istekno.gohipeandroidapp.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivityLoginRegisterBinding
import com.istekno.gohipeandroidapp.fragments.LoginScreenFragment
import com.istekno.gohipeandroidapp.fragments.SelectRoleFragment

class LoginRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_register)
        supportActionBar?.hide()
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )

        binding.topAppBarLogregact.setNavigationOnClickListener {
            onBackPressed()
        }

        bindFragment()
    }

    private fun bindFragment() {

        when (intent.getIntExtra("Codename Logreg", -1)) {
            0 -> {
                val fragment = supportFragmentManager.findFragmentByTag(LoginScreenFragment::class.java.simpleName)
                if (fragment !is LoginScreenFragment) {
                    supportFragmentManager.beginTransaction().add(R.id.frame_container_logregact, LoginScreenFragment()).commit()
                }
            }
            1 -> {
                val fragment = supportFragmentManager.findFragmentByTag(SelectRoleFragment::class.java.simpleName)
                if (fragment !is SelectRoleFragment) {
                    supportFragmentManager.beginTransaction().add(R.id.frame_container_logregact, SelectRoleFragment()).commit()
                }
            }
        }
    }
}