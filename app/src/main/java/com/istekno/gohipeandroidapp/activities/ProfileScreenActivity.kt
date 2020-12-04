package com.istekno.gohipeandroidapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.fragments.EngineerProfileScreenFragment

class ProfileScreenActivity : AppCompatActivity() {
    
    private val mFragmentManager = supportFragmentManager
    private val mFragment = EngineerProfileScreenFragment()
    private val fragment = mFragmentManager.findFragmentByTag(EngineerProfileScreenFragment::class.java.simpleName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )

        if (fragment !is EngineerProfileScreenFragment) {
            mFragmentManager.beginTransaction().apply {
                add(R.id.frame_container_profile, mFragment, EngineerProfileScreenFragment::class.java.simpleName)
                commit()
            }
        }
    }
}