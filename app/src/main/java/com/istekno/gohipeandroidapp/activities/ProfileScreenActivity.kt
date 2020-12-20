package com.istekno.gohipeandroidapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.data.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.ActivityProfileScreenBinding
import com.istekno.gohipeandroidapp.fragments.CompanyDetailProfileScreenFragment
import com.istekno.gohipeandroidapp.fragments.EngineerDetailProfileScreenFragment
import com.istekno.gohipeandroidapp.fragments.LoginScreenFragment.Companion.CODENAME1
import com.istekno.gohipeandroidapp.fragments.LoginScreenFragment.Companion.CODENAME2

class ProfileScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_screen)
        supportActionBar?.hide()

//        bindFragment()
        authentication()

        binding.topAppBarProfileact.setNavigationOnClickListener {
            onBackPressed()
        }
    }

//    private fun bindFragment() {
//        val mFragmentManager = supportFragmentManager
//        val mFragment : Fragment
//        val role = intent.getIntExtra("Codename Profile", -1)
//
//        when (role) {
//            0 -> {
//                val fullname = intent.getStringExtra(CODENAME1_ENG_REG_FULLNAME)
//                val email = intent.getStringExtra(CODENAME2_ENG_REG_EMAIL)
//
//                val fragment = mFragmentManager.findFragmentByTag(EngineerDetailProfileScreenFragment::class.java.simpleName)
//                mFragment = EngineerDetailProfileScreenFragment(fullname, email)
//                if (fragment !is EngineerDetailProfileScreenFragment) {
//                    mFragmentManager.beginTransaction().apply {
//                        add(R.id.frame_container_profileact, mFragment, EngineerDetailProfileScreenFragment::class.java.simpleName)
//                        commit()
//                    }
//                }
//            }
//            1 -> {
//                val fullname = intent.getStringExtra(CODENAME1_COMP_REG_FULLNAME)
//                val email = intent.getStringExtra(CODENAME2_COMP_REG_EMAIL)
//                val position = intent.getStringExtra(CODENAME6_COMP_REG_POSITION)
//
//                val fragment = mFragmentManager.findFragmentByTag(CompanyDetailProfileScreenFragment::class.java.simpleName)
//                mFragment = CompanyDetailProfileScreenFragment(fullname, email, position)
//                if (fragment !is CompanyDetailProfileScreenFragment) {
//                    mFragmentManager.beginTransaction().apply {
//                        add(R.id.frame_container_profileact, mFragment, CompanyDetailProfileScreenFragment::class.java.simpleName)
//                        commit()
//                    }
//                }
//            }
//        }
//    }

    private fun authentication() {
        val listEmailENG = GoHipeDatabases.loginEmailEngineer
        val listPasswordENG = GoHipeDatabases.loginPasswordEngineer
        val listEmailCOMP = GoHipeDatabases.loginEmailCompany
        val listPasswordCOMP = GoHipeDatabases.loginPasswordCompany

        val email = intent.getStringExtra(CODENAME1)
        val password = intent.getStringExtra(CODENAME2)

        if (listEmailENG.contains(email) && listPasswordENG.contains(password)) {
            val fragment = supportFragmentManager.findFragmentByTag(EngineerDetailProfileScreenFragment::class.java.simpleName)
            if (fragment !is EngineerDetailProfileScreenFragment) {
                supportFragmentManager.beginTransaction().add(R.id.frame_container_profileact, EngineerDetailProfileScreenFragment(email, password)).commit()
            }
        } else if (listEmailCOMP.contains(email) && listPasswordCOMP.contains(password)) {
            val fragment = supportFragmentManager.findFragmentByTag(CompanyDetailProfileScreenFragment::class.java.simpleName)
            if (fragment !is CompanyDetailProfileScreenFragment) {
                supportFragmentManager.beginTransaction().add(R.id.frame_container_profileact, CompanyDetailProfileScreenFragment(email, password)).commit()
            }
        }

    }
}