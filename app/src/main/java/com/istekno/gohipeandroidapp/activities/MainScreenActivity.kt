package com.istekno.gohipeandroidapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.fragments.MainScreenFragment

class MainScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        val mFragmentManager = supportFragmentManager
        val mFragment = MainScreenFragment()
        val fragment = mFragmentManager.findFragmentByTag(MainScreenFragment::class.java.simpleName)

        if (fragment !is MainScreenFragment) {
            mFragmentManager.beginTransaction().apply {
                add(R.id.frame_container, mFragment, MainScreenFragment::class.java.simpleName)
                commit()
            }
        }
    }
}