package com.istekno.gohipeandroidapp.helpers

import android.widget.SearchView

class SearchProject {

    private lateinit var onQueryTextListener: OnQueryTextListener

    fun setOnQueryListener(onQueryTextListener: OnQueryTextListener) {
        this.onQueryTextListener = onQueryTextListener
    }

    interface OnQueryTextListener {
        fun onQueryChangeListener(query: String)
        fun onQuerySubmitListener(query: String)
        fun onCloseListener()
    }

    fun searchByUsername(searchView: SearchView) {
        searchView.isIconified = true
        searchView.queryHint = "Search name"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(str: String?): Boolean {
                if (str != null) {
                    onQueryTextListener.onQuerySubmitListener(str)
                }
                return true
            }

            override fun onQueryTextChange(str: String?): Boolean {
                if (str != null) {
                    onQueryTextListener.onQueryChangeListener(str)
                }
                return false
            }
        })

        searchView.setOnCloseListener {
            searchView.onActionViewCollapsed()
            onQueryTextListener.onCloseListener()
            return@setOnCloseListener true
        }
    }
}