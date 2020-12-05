package com.istekno.gohipeandroidapp.data

import com.istekno.gohipeandroidapp.R

object GoHipeDatabases {
    private val portfolio = intArrayOf(
        R.drawable.img_porto1,
        R.drawable.img_porto2,
        R.drawable.img_porto3,
        R.drawable.img_porto4,
        R.drawable.img_porto5
    )

    private val job = arrayOf(
        "Software Engineer",
        "QA Developer",
        "Fullstack Website",
        "Android Developer",
        "Product Designer"
    )

    private val company = arrayOf(
        "Google Inc.",
        "Line Corp.",
        "Tesla Eco Inc.",
        "Walt Disney Corp.",
        "SONY PlayStation Inc."
    )

    private val period = arrayOf(
        "July 2017 - January 2019",
        "March 2016 - April 2018",
        "June 2018 - October 2019",
        "January 2015 - March 2016",
        "August 2019 - December 2020"
    )

    private val totalMonth = arrayOf(
        "16 months",
        "23 months",
        "16 months",
        "14 months",
        "16 months"
    )

    private const val str = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
    private val desc = arrayOf(
        str,
        str,
        str,
        str,
        str
    )


    val porto : IntArray
    get() {
        val portos = portfolio
        return portos
    }

    val listExperiences : ArrayList<Experience>
    get() {
        val expers = ArrayList<Experience>()
        for (i in desc.indices) {
            val experience = Experience(
                portfolio[i],
                job[i],
                company[i],
                period[i],
                totalMonth[i],
                desc[i]
            )
            expers.add(experience)
        }
        return expers
    }
}