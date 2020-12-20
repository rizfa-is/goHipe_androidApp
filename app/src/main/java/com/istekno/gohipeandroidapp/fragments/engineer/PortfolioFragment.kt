package com.istekno.gohipeandroidapp.fragments.engineer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListPortfolioRecycleViewAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.models.Portfolio
import com.istekno.gohipeandroidapp.databinding.FragmentPortfolioBinding

class PortfolioFragment : Fragment() {

    private lateinit var binding: FragmentPortfolioBinding

    private val listPortfolio = ArrayList<Portfolio>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val portfolio = GoHipeDatabases.porto

        for (i in 0 until portfolio.size) {
            val porto = Portfolio(
                portfolio[i]
            )
            listPortfolio.add(porto)
        }
        showRecycleList(view)
    }

    private fun showRecycleList(view: View) {
        binding.rvPortofrg.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = ListPortfolioRecycleViewAdapter(listPortfolio)
        }
    }
}