package com.example.bsch_md_ass1.db

import android.content.ContentValues

data class BookCopy(var book : Book, var loaner : Loaner? = null, val ID : Int? = null) {
    fun toContentValues() : ContentValues {
        val cv = ContentValues()
        cv.put(DBController.CopiesTable.columnNames[DBController.CopiesTable.BookID], book.id)
        cv.put(DBController.CopiesTable.columnNames[DBController.CopiesTable.Loaner], loaner?.ID)
        return cv
    }
}

