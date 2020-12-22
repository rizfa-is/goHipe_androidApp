package com.istekno.gohipeandroidapp.fragments.company

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyAccountScreenBinding
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerAccountScreenFragment

class CompanyAccountScreenFragment(private val toolbar: MaterialToolbar) : Fragment() {

    companion object {
        const val SETTING_AUTH_KEY = "setting_auth_key"
    }

    private lateinit var binding: FragmentCompanyAccountScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_account_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.mn_maincontent_toolbar_setting) {
                val sendIntent = Intent(context, SettingScreenActivity::class.java)
                sendIntent.putExtra(SETTING_AUTH_KEY, 1)
                startActivity(sendIntent)
            }
            false
        }
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.visibility = View.VISIBLE
        toolbar.title = "My Account"
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = true
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = true
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }
}