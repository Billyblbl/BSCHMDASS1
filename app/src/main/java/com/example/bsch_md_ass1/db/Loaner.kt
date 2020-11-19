package com.example.bsch_md_ass1.db

import android.content.ContentValues

data class Loaner(val fullname : String? = null, val ID : Int? = null, var loanedBooks : Int = 0) {
    fun toContentValues() : ContentValues {
        val cv = ContentValues()
        cv.put(DBController.LoanersTable.columnNames[DBController.LoanersTable.FullName], fullname)
        cv.put(DBController.LoanersTable.columnNames[DBController.LoanersTable.LoanedBooks], loanedBooks)
        return cv
    }
}
