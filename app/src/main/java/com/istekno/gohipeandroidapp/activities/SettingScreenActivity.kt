package com.istekno.gohipeandroidapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivitySettingScreenBinding
import com.istekno.gohipeandroidapp.fragments.EngineerAccountSettingFragment

class SettingScreenActivity : AppCompatActivity() {

    private lateinit var settingScreenBinding: ActivitySettingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting_screen)
        setSupportActionBar(settingScreenBinding.topAppBarSetact)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction().replace(R.id.frame_container_setact, EngineerAccountSettingFragment()).commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}