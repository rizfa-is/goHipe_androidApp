package com.istekno.gohipeandroidapp.fragments.company

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyAccountScreenBinding
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerAccountScreenFragment
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.CompanyGetByIDResponse
import com.istekno.gohipeandroidapp.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class CompanyAccountScreenFragment(private val toolbar: MaterialToolbar, private val bottomNavigationView: BottomNavigationView, private val co: CoordinatorLayout) : Fragment() {

    companion object {
        const val SETTING_AUTH_KEY = "setting_auth_key"
        const val EDIT_PROFILE_AUTH_KEY = "edit_profile_auth_key"
    }

    private lateinit var binding: FragmentCompanyAccountScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar, co)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_account_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)

        getCompanyInfo(view)
        viewListener()

    }

    private fun getCompanyInfo(view: View) {
        coroutineScope.launch {
            val id = goHipePreferences.getCompanyPreference().acID

            val result = withContext(Dispatchers.IO) {
                try {
                    service.getCompanyByID(id!!.toLong())
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is CompanyGetByIDResponse) {
                binding.model = result.database!![0]
                Glide.with(view.context).load(EngineerAccountScreenFragment.imageLink + result.database[0].cpAvatar).into(binding.imgComaccfrgAvatar)
            }
        }
    }

    private fun viewListener() {
        binding.btnComaccfrgEditprofile.setOnClickListener {
            val sendIntent = Intent(context, SettingScreenActivity::class.java)
            sendIntent.putExtra(EDIT_PROFILE_AUTH_KEY, 1)
            startActivity(sendIntent)
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.mn_maincontent_toolbar_favorite -> {
                    fragmentManager?.beginTransaction()?.replace(R.id.frame_container_maincontent, CompanyFavoriteScreenFragment(toolbar, bottomNavigationView))?.addToBackStack(null)?.commit()
                }
                R.id.mn_maincontent_toolbar_setting -> {
                    val sendIntent = Intent(context, SettingScreenActivity::class.java)
                    sendIntent.putExtra(SETTING_AUTH_KEY, 1)
                    startActivity(sendIntent)
                }
            }
            false
        }
    }

    private fun setToolbar(toolbar: MaterialToolbar, co: CoordinatorLayout) {
        bottomNavigationView.visibility = View.VISIBLE
        co.visibility = View.VISIBLE
        toolbar.title = "My Account"
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = true
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = true
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }
}