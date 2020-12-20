package com.istekno.gohipeandroidapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentChatBinding

class ChatFragment(private val toolbar: MaterialToolbar) : Fragment() {

    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        return binding.root
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
    }
}