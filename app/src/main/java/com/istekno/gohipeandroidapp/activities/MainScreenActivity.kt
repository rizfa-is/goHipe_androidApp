package com.istekno.gohipeandroidapp.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.fragments.MainScreenFragment

class MainScreenActivity : AppCompatActivity() {

    private val mFragmentManager = supportFragmentManager
    private val mFragment = MainScreenFragment()
    private val fragment = mFragmentManager.findFragmentByTag(MainScreenFragment::class.java.simpleName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )

        if (fragment !is MainScreenFragment) {
            mFragmentManager.beginTransaction().apply {
                add(R.id.frame_container, mFragment, MainScreenFragment::class.java.simpleName)
                commit()
            }
        }
    }
}