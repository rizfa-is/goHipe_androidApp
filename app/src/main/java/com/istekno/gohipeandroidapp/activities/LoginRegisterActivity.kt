package com.istekno.gohipeandroidapp.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.fragments.LoginScreenFragment
import com.istekno.gohipeandroidapp.fragments.SelectRoleFragment
import kotlinx.android.synthetic.main.activity_login_register.*

class LoginRegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
        supportActionBar?.hide()
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )

        bindFragment()

        topAppBar_logregact.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun bindFragment() {
        val mFragmentManager = supportFragmentManager
        val mFragment : Fragment
        val role = intent.getIntExtra("Codename Logreg", -1)

        when (role) {
            0 -> {
                val fragment = mFragmentManager.findFragmentByTag(LoginScreenFragment::class.java.simpleName)
                mFragment = LoginScreenFragment()
                if (fragment !is LoginScreenFragment) {
                    mFragmentManager.beginTransaction().apply {
                        add(R.id.frame_container_logregact, mFragment, LoginScreenFragment::class.java.simpleName)
                        commit()
                    }
                }
            }
            1 -> {
                val fragment = mFragmentManager.findFragmentByTag(SelectRoleFragment::class.java.simpleName)
                mFragment = SelectRoleFragment()
                if (fragment !is SelectRoleFragment) {
                    mFragmentManager.beginTransaction().apply {
                        add(R.id.frame_container_logregact, mFragment, SelectRoleFragment::class.java.simpleName)
                        commit()
                    }
                }
            }
        }
    }
}