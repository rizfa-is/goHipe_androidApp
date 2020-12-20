package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.MainScreenActivity
import com.istekno.gohipeandroidapp.models.EngineerModel
import com.istekno.gohipeandroidapp.databases.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerAccountSettingBinding
import com.istekno.gohipeandroidapp.utility.Dialog

class EngineerAccountSettingFragment: Fragment() {

    private lateinit var binding: FragmentEngineerAccountSettingBinding
    private lateinit var engineerModel: EngineerModel
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_account_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = Dialog()
        goHipePreferences = GoHipePreferences(view.context)
        engineerModel = goHipePreferences.getEngineerPreference()

        binding.btnEngsetfrgLogout.setOnClickListener {
            if (goHipePreferences.getEngineerPreference().isLogin) {
                engineerModel.isLogin = false
                goHipePreferences.setEngineerPreference(engineerModel)

                dialog.dialog(view.context, "Log Out Successful") {
                    startActivity(Intent(view.context, MainScreenActivity::class.java))
                    activity?.finish()
                }
            }
        }
    }
}