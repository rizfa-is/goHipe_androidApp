package com.istekno.gohipeandroidapp.helpers

import com.istekno.gohipeandroidapp.retrofit.HireModelResponse

class SearchProject {

    private lateinit var onQueryTextListener: OnQueryTextListener

    fun setOnQueryListener(onQueryTextListener: OnQueryTextListener) {
        this.onQueryTextListener = onQueryTextListener
    }

    interface OnQueryTextListener {
        fun onQueryChangeListener(query: String)
        fun onQuerySubmitListener(query: String)
    }

    fun searchByUsername(searchView: android.widget.SearchView) {
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(str: String?): Boolean {
                if (str != null) {
                    if (str.isEmpty()) return false
                    onQueryTextListener.onQuerySubmitListener(str)
                }
                return false
            }

            override fun onQueryTextChange(str: String?): Boolean {
                if (str != null) {
                    if (str.isEmpty()) return false
                    onQueryTextListener.onQueryChangeListener(str)
                }
                return false
            }
        })
    }
}