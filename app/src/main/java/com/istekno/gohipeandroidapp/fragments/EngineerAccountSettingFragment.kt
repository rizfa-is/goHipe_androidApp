package com.istekno.gohipeandroidapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.LoginRegisterActivity
import com.istekno.gohipeandroidapp.activities.MainScreenActivity
import com.istekno.gohipeandroidapp.data.EngineerModel
import com.istekno.gohipeandroidapp.databases.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerAccountSettingBinding

class EngineerAccountSettingFragment: Fragment() {

    private lateinit var binding: FragmentEngineerAccountSettingBinding
    private lateinit var engineerModel: EngineerModel
    private lateinit var goHipePreferences: GoHipePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_account_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goHipePreferences = GoHipePreferences(view.context)
        engineerModel = goHipePreferences.getEngineerPreference()

        binding.btnEngsetfrgLogout.setOnClickListener {
            if (goHipePreferences.getEngineerPreference().isLogin) {
                engineerModel.isLogin = false
                goHipePreferences.setEngineerPreference(engineerModel)
                startActivity(Intent(view.context, MainScreenActivity::class.java))
                activity?.finish()
            }
        }
    }
}