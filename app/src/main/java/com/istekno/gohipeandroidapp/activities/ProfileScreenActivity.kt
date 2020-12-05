package com.istekno.gohipeandroidapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.fragments.CompanyProfileScreenFragment
import com.istekno.gohipeandroidapp.fragments.EngineerProfileScreenFragment
import com.istekno.gohipeandroidapp.fragments.LoginScreenFragment
import com.istekno.gohipeandroidapp.fragments.SelectRoleFragment
import kotlinx.android.synthetic.main.activity_profile_screen.*

class ProfileScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)
        supportActionBar?.hide()

        bindFragment()

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
}