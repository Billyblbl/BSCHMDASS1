package com.example.bsch_md_ass1.db

import android.content.ContentValues

data class Book(
        val id : Int? = null,
        val title : String? = null,
        val author : String? = null,
        val genre : String? = null,
        var copiesAmount : Int? = null,
        var loanedCopies : Int? = null
) {
    val availables : Int?
        get() = loanedCopies?.let { copiesAmount?.minus(it) }

    fun toContentValues() : ContentValues {
        val cv = ContentValues()
        cv.put(DBController.BooksTable.columnNames[DBController.BooksTable.Title], title)
        cv.put(DBController.BooksTable.columnNames[DBController.BooksTable.Author], author)
        cv.put(DBController.BooksTable.columnNames[DBController.BooksTable.Genre], genre)
        if (copiesAmount != null) cv.put(DBController.BooksTable.columnNames[DBController.BooksTable.CopiesAmount], copiesAmount)
        if (loanedCopies != null) cv.put(DBController.BooksTable.columnNames[DBController.BooksTable.LoanedCopies], loanedCopies)
        return cv
    }
}
