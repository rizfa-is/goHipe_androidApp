package com.istekno.gohipeandroidapp.helpers

class VariableGetSet {

    private var deadline = ""

    fun setData(date: String) {
        this.deadline = date
    }

    fun getData(): String {
        return deadline
    }
}