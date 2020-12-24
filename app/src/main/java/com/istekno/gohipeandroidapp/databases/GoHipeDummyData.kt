package com.istekno.gohipeandroidapp.databases

import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.models.Experience
import com.istekno.gohipeandroidapp.models.MostPopular
import com.istekno.gohipeandroidapp.models.ScouterTop
import com.istekno.gohipeandroidapp.models.User

object GoHipeDatabases {
    private val portfolio = intArrayOf(
        R.drawable.img_porto1,
        R.drawable.img_porto2,
        R.drawable.img_porto3,
        R.drawable.img_porto4,
        R.drawable.img_porto5
    )

    private val name = arrayOf(
            "Portgas D. Ace",
            "Monkey D. Luffy",
            "Uzumaki Naruto",
            "Vinsmoke Sanji",
            "Meito Shusui"
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

    private val fieldComp = arrayOf(
        "Fintech.",
        "Software House",
        "E-Commerce",
        "Health",
        "Literature"
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

    private val project = arrayOf(10, 11, 9, 27, 18)
    private val day = arrayOf(6, 8, 5, 4, 7)
    private val rate = arrayOf(88, 90, 96, 91, 93)

    private val eng = arrayOf(9, 7, 8, 14, 11)
    private val reqrate = arrayOf(8.8, 9.0, 9.6, 9.1, 9.3)

    private val listEngineerAbilities = arrayOf(
        listOf("Android", "108 MP", "Creativity"),
        listOf("ReactJs", "AI", "Angular", "Route", "Kotlin", "Coroutines"),
        listOf("iOS", "Swift", "Creativity", "Route"),
        listOf("GoLang", "Java", "Scrum", "Route", "AI"),
        listOf("Ruby", "NetBeans", "Spring", "Route", "AI")
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

    val listEngineerTalentOfTheMonth: ArrayList<MostPopular>
    get() {
        val mp = ArrayList<MostPopular>()
        for (i in name.indices) {
            val mostPopular = MostPopular(
                    name[i],
                    job[i],
                    portfolio[i],
                    project[i],
                    day[i],
                    rate[i]
            )
            mp.add(mostPopular)
        }
        return mp
    }

    val listScouterOfTheMonth: ArrayList<ScouterTop>
        get() {
            val mp = ArrayList<ScouterTop>()
            for (i in company.indices) {
                val mostPopular = ScouterTop(
                    company[i],
                    fieldComp[i],
                    portfolio[i],
                    project[i],
                    eng[i],
                    reqrate[i]
                )
                mp.add(mostPopular)
            }
            return mp
        }

    val listSearchEngineer: ArrayList<User>
        get() {
            val mp = ArrayList<User>()
            for (i in name.indices) {
                val mostPopular = User(
                    name[i],
                    job[i],
                    portfolio[i],
                    listEngineerAbilities[i]
                )
                mp.add(mostPopular)
            }
            return mp
        }

    private val abilities = listOf(
        "Android",
        "108 MP",
        "Creativity",
        "Route",
        "AI"
    )

    val ability : List<String>
    get() {
        return abilities
    }
}