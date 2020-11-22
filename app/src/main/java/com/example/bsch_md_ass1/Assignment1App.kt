package com.example.bsch_md_ass1

import android.app.Application
import com.example.bsch_md_ass1.db.DBController
import com.example.bsch_md_ass1.db.Loaner

class Assignment1App : Application() {
    lateinit var db : DBController
    lateinit var currentUser : Loaner
}
