package com.istekno.gohipeandroidapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.data.GoHipeDatabases
import com.istekno.gohipeandroidapp.fragments.CompanyProfileScreenFragment
import com.istekno.gohipeandroidapp.fragments.EngineerProfileScreenFragment
import com.istekno.gohipeandroidapp.fragments.LoginScreenFragment.Companion.CODENAME1
import com.istekno.gohipeandroidapp.fragments.LoginScreenFragment.Companion.CODENAME2
import kotlinx.android.synthetic.main.activity_profile_screen.*

class ProfileScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)
        supportActionBar?.hide()

        bindFragment()
        authentication()

        topAppBar_profileact.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun bindFragment() {
        val mFragmentManager = supportFragmentManager
        val mFragment : Fragment
        val role = intent.getIntExtra("Codename Profile", -1)

        when (role) {
            0 -> {
                val fragment = mFragmentManager.findFragmentByTag(EngineerProfileScreenFragment::class.java.simpleName)
                mFragment = EngineerProfileScreenFragment()
                if (fragment !is EngineerProfileScreenFragment) {
                    mFragmentManager.beginTransaction().apply {
                        add(R.id.frame_container_profileact, mFragment, EngineerProfileScreenFragment::class.java.simpleName)
                        commit()
                    }
                }
            }
            1 -> {
                val fragment = mFragmentManager.findFragmentByTag(CompanyProfileScreenFragment::class.java.simpleName)
                mFragment = CompanyProfileScreenFragment()
                if (fragment !is CompanyProfileScreenFragment) {
                    mFragmentManager.beginTransaction().apply {
                        add(R.id.frame_container_profileact, mFragment, CompanyProfileScreenFragment::class.java.simpleName)
                        commit()
                    }
                }
            }
        }
    }

    private fun authentication() {
        val listEmailENG = GoHipeDatabases.loginEmailEngineer
        val listPasswordENG = GoHipeDatabases.loginPasswordEngineer
        val listEmailCOMP = GoHipeDatabases.loginEmailCompany
        val listPasswordCOMP = GoHipeDatabases.loginPasswordCompany

        val email = intent.getStringExtra(CODENAME1)
        val password = intent.getStringExtra(CODENAME2)

        val mFragmentManager = supportFragmentManager
        val mFragment : Fragment

        if (listEmailENG.contains(email) && listPasswordENG.contains(password)) {
            val fragment = mFragmentManager.findFragmentByTag(EngineerProfileScreenFragment::class.java.simpleName)
            mFragment = EngineerProfileScreenFragment(email, password)
            if (fragment !is EngineerProfileScreenFragment) {
                mFragmentManager.beginTransaction().apply {
                    add(R.id.frame_container_profileact, mFragment, EngineerProfileScreenFragment::class.java.simpleName)
                    commit()
                }
            }
        } else if (listEmailCOMP.contains(email) && listPasswordCOMP.contains(password)) {
            val fragment = mFragmentManager.findFragmentByTag(CompanyProfileScreenFragment::class.java.simpleName)
            mFragment = CompanyProfileScreenFragment(email, password)
            if (fragment !is CompanyProfileScreenFragment) {
                mFragmentManager.beginTransaction().apply {
                    add(R.id.frame_container_profileact, mFragment, CompanyProfileScreenFragment::class.java.simpleName)
                    commit()
                }
            }
        }

    }
}