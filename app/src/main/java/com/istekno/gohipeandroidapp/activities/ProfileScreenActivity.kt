package com.istekno.gohipeandroidapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.data.GoHipeDatabases
import com.istekno.gohipeandroidapp.fragments.CompanyDetailProfileScreenFragment
import com.istekno.gohipeandroidapp.fragments.CompanyRegisterScreenFragment.Companion.CODENAME1_COMP_REG_FULLNAME
import com.istekno.gohipeandroidapp.fragments.CompanyRegisterScreenFragment.Companion.CODENAME2_COMP_REG_EMAIL
import com.istekno.gohipeandroidapp.fragments.CompanyRegisterScreenFragment.Companion.CODENAME6_COMP_REG_POSITION
import com.istekno.gohipeandroidapp.fragments.EngineerDetailProfileScreenFragment
import com.istekno.gohipeandroidapp.fragments.LoginScreenFragment.Companion.CODENAME1
import com.istekno.gohipeandroidapp.fragments.LoginScreenFragment.Companion.CODENAME2
import kotlinx.android.synthetic.main.activity_profile_screen.*

class ProfileScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)
        supportActionBar?.hide()

//        bindFragment()
        authentication()

        topAppBar_profileact.setNavigationOnClickListener {
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

        val mFragmentManager = supportFragmentManager
        val mFragment : Fragment

        if (listEmailENG.contains(email) && listPasswordENG.contains(password)) {
            val fragment = mFragmentManager.findFragmentByTag(EngineerDetailProfileScreenFragment::class.java.simpleName)
            mFragment = EngineerDetailProfileScreenFragment(email, password)
            if (fragment !is EngineerDetailProfileScreenFragment) {
                mFragmentManager.beginTransaction().apply {
                    add(R.id.frame_container_profileact, mFragment, EngineerDetailProfileScreenFragment::class.java.simpleName)
                    commit()
                }
            }
        } else if (listEmailCOMP.contains(email) && listPasswordCOMP.contains(password)) {
            val fragment = mFragmentManager.findFragmentByTag(CompanyDetailProfileScreenFragment::class.java.simpleName)
            mFragment = CompanyDetailProfileScreenFragment(email, password)
            if (fragment !is CompanyDetailProfileScreenFragment) {
                mFragmentManager.beginTransaction().apply {
                    add(R.id.frame_container_profileact, mFragment, CompanyDetailProfileScreenFragment::class.java.simpleName)
                    commit()
                }
            }
        }

    }
}