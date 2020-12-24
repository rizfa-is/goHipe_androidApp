package com.istekno.gohipeandroidapp.fragments.engineer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerNotificationScreenBinding

class EngineerNotificationScreenFragment(private val toolbar: MaterialToolbar, private val bottomNavigationView: BottomNavigationView) : Fragment() {

    private lateinit var binding: FragmentEngineerNotificationScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar, bottomNavigationView)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_notification_screen, container, false)
        return binding.root
    }

    private fun setToolbar(toolbar: MaterialToolbar, bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.visibility = View.GONE
        toolbar.visibility = View.VISIBLE
        toolbar.title = "Notification"
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }
}