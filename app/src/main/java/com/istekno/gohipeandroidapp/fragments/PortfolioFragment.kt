package com.istekno.gohipeandroidapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListPortfolioRecycleViewAdapter
import com.istekno.gohipeandroidapp.data.Portfolio
import kotlinx.android.synthetic.main.fragment_portfolio.*

class PortfolioFragment : Fragment() {

    private val listPortfolio = ArrayList<Portfolio>()
    private val portfolio = intArrayOf(
        R.drawable.img_porto1,
        R.drawable.img_porto2,
        R.drawable.img_porto3,
        R.drawable.img_porto4,
        R.drawable.img_porto5
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (i in 0 until portfolio.size) {
            val porto = Portfolio(
                portfolio[i]
            )
            listPortfolio.add(porto)
        }
        showRecycleList()
    }

    private fun showRecycleList() {
        rv_portofrg.apply {
            layoutManager = LinearLayoutManager(view?.context)
            adapter = ListPortfolioRecycleViewAdapter(listPortfolio)
        }
    }
}