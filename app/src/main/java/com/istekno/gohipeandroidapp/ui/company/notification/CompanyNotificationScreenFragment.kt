package com.istekno.gohipeandroidapp.ui.company.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyNotificationScreenBinding

class CompanyNotificationScreenFragment(private val toolbar: MaterialToolbar, private val bottomNavigationView: BottomNavigationView, private val co: CoordinatorLayout) : Fragment() {

    private lateinit var binding: FragmentCompanyNotificationScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar, bottomNavigationView, co)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_notification_screen, container, false)
        return binding.root
    }

    private fun setToolbar(toolbar: MaterialToolbar, bottomNavigationView: BottomNavigationView, co: CoordinatorLayout) {
        bottomNavigationView.visibility = View.GONE
        co.visibility = View.VISIBLE
        toolbar.title = "Notification"
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }
}