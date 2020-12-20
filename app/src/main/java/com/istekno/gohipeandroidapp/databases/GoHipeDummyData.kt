package com.istekno.gohipeandroidapp.databases

import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.models.Experience

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


    private val abilities = arrayOf(
            "Android",
            "108 MP",
            "Creativity",
            "Route"
    )

    val ability : Array<String>
    get() {
        return abilities
    }

    private val iconAbilities = intArrayOf(
            R.drawable.ic_android,
            R.drawable.ic_camera,
            R.drawable.ic_idea,
            R.drawable.ic_location_track
    )

    val iconAbility : IntArray
        get() {
            return iconAbilities
        }

    //LOGIN
    //ENGINEER

    private val emailEng = arrayOf(
        "rosyidrosadi15@gmail.com"
    )

    val loginEmailEngineer : Array<String>
    get() {
        return emailEng
    }

    private val passwordEng = arrayOf(
        "qwer1234"
    )

    val loginPasswordEngineer : Array<String>
    get() {
        return passwordEng
    }

    //COMPANY

    private val emailComp = arrayOf(
        "siosin15@gmail.com"
    )

    val loginEmailCompany : Array<String>
        get() {
            return emailComp
        }

    private val passwordComp = arrayOf(
        "asdf1234"
    )

    val loginPasswordCompany : Array<String>
        get() {
            return passwordComp
        }
}