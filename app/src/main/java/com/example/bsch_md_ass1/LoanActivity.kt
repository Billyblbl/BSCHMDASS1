package com.example.bsch_md_ass1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bsch_md_ass1.db.BookCopy

class LoanActivity : AppCompatActivity() {

    val copiesList = ArrayList<BookCopy>()
    lateinit var adapter : CopyListAdapter
    lateinit var app : Assignment1App

    var bookID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loan_activity)

        app = application as Assignment1App

        val intent = this.intent
        bookID = intent.getIntExtra(BOOKID, -1)

        val book = app.db.getBook(bookID)

        adapter = object : CopyListAdapter(this, copiesList, app.currentUser) {
            override fun BookCopy.interact() {
                if (loaner != null && loaner?.ID == currentUser.ID) {
                    app.db.returnLoan(this)
                } else {
                    app.db.loan(app.currentUser, this)
                }
                this@LoanActivity.updateCopiesList()
            }
        }

        findViewById<TextView>(R.id.copies_book).text = book.title
        findViewById<ListView>(R.id.copiesList).adapter = adapter

        updateCopiesList()
    }

    fun updateCopiesList() {
        copiesList.clear()
        copiesList.addAll(app.db.getCopiesOf(app.db.getBook(bookID)))
        /*
        Log.d("Copies: ", copiesList.count().toString())
        for (copy in copiesList) {
            Log.d("Copy: ", "{ id: ${copy.ID}, book: ${copy.book.id}, loaner: ${copy.loaner?.ID}")
        }
         */
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        updateCopiesList()
    }

    companion object {

        const val BOOKID = "BookID"

        fun start(context: Context, bookID: Int) {
            val intent = Intent(context, LoanActivity::class.java)
            intent.putExtra(BOOKID, bookID)
            context.startActivity(intent)
        }
    }

}