package com.istekno.gohipeandroidapp.fragments.company

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.MainContentActivity
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.activities.SplashScreenActivity
import com.istekno.gohipeandroidapp.adapter.UserDetailProfileAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyHomeScreenBinding
import com.istekno.gohipeandroidapp.models.User

class CompanyHomeScreenFragment(private val toolbar: MaterialToolbar) : Fragment() {

    companion object {
        const val HOME_AUTH_KEY = "home_auth_key"
    }

    private lateinit var binding: FragmentCompanyHomeScreenBinding
    private val listUser = ArrayList<User>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_home_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listUser.add(User("Monkey D Luffy", "Android Developer", R.drawable.img_mainscreen_background))
        showRecyclerList()
    }

    private fun showRecyclerList() {
        binding.rvListDeveloper.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserDetailProfileAdapter(listUser, object : UserDetailProfileAdapter.OnItemClickCallback {
                override fun onItemClicked(user: User) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(HOME_AUTH_KEY, 1)
                    startActivity(sendIntent)
                }
            })
        }
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
        toolbar.title = "Home"
    }

}