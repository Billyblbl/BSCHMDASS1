package com.example.bsch_md_ass1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bsch_md_ass1.db.Book
import com.example.bsch_md_ass1.db.BookCopy
import com.example.bsch_md_ass1.db.DBController
import com.example.bsch_md_ass1.db.Loaner
import java.util.ArrayList

class BooksActivity : AppCompatActivity() {

    lateinit var db : DBController

    val bookList = ArrayList<Book>()
    lateinit var bookListAdapter : BookListAdapter
    lateinit var app : Assignment1App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.books_activity)

        //TODO correctly manage error instead of this dirty englobing try statement
        app = application as Assignment1App

        //GUI
        val bookListView = findViewById<ListView>(R.id.book_list)
        bookListAdapter = BookListAdapter(this, bookList)
        bookListView.adapter = bookListAdapter

        //Database
        db = DBController(this, "library.db", null, 1)
        app.db = db
        db.onCreate(db.writableDatabase)

        //user interactions
        bookListView.setOnItemClickListener { _, _, position, _ ->
            val book = bookListAdapter.getItem(position)
            if (book != null && book.availables!! > 0) LoanActivity.start(this, book.id!!)
            else if (book != null) Toast.makeText(this, "No available copy for the book \"${book.title}\"", Toast.LENGTH_SHORT).show()
        }

        initDemoState()
        updateBookList()
    }

    override fun onStart() {
        super.onStart()
        updateBookList()
    }

    override fun onResume() {
        super.onResume()
        updateBookList()
    }

    /**
     * This function updates the GUI elements displaying the list of books
     *
     */
    fun updateBookList() {
        bookList.clear()

        // this commented code was meant to ensure that only available books were displayed,
        // however displaying the loaned books gives more info to the user, and can allow him
        // vaguely evaluate an estimation for when a book is have available copies (ex : if
        // the book has a lot of copies, it is more likely that one will be returned in the
        // near future) so for now we keep displaying the whole book list with stats

        //bookList.addAll(db.allAvailableBooks)
        bookList.addAll(db.allBooks)
        bookListAdapter.notifyDataSetChanged()
    }

    /**
     * this method is only here to setup values in the database needed for demo purposes,
     * as this is an exercise and in no way any kind of consumer ready product
     * these values are mostly random.
     */
    fun initDemoState() {
        //Db values
        for (book in arrayOf(
                Book(title = "A Book", author = "Hector Ducon-la-joie", genre = "Sci-Fi"),
                Book(title = "Another Book", author = "Hector Ducon-la-joie", genre = "Sci-Fi"),
                Book(title = "And another", author = "Philippe Echevier", genre = "Fantasy")
        )) db.addBook(book)

        for (loaner in arrayOf(
                Loaner(fullname = "Jean-Michel Meunier"),
                Loaner(fullname = "Jean-Eudes Parmier"),
                Loaner(fullname = "Patrick Delajoue"),
                Loaner(fullname = "Amanda Lefevre")
        )) db.addLoaner(loaner)

        for (copy in arrayOf(
                BookCopy(db.allBooks[0]),
                BookCopy(db.allBooks[0]),
                BookCopy(db.allBooks[0]),
                BookCopy(db.allBooks[0]),
                BookCopy(db.allBooks[0]),
                BookCopy(db.allBooks[0]),
                BookCopy(db.allBooks[0]),
                BookCopy(db.allBooks[1]),
                BookCopy(db.allBooks[1]),
                BookCopy(db.allBooks[1]),
                BookCopy(db.allBooks[1]),
                BookCopy(db.allBooks[1]),
                BookCopy(db.allBooks[1]),
                BookCopy(db.allBooks[2]),
                BookCopy(db.allBooks[2])
        )) db.addCopy(copy)

        //Current user as the first loaner in the DB
        app.currentUser = db.allLoaners[0]
    }

    //TODO for testing purposes, remove for delivery
    override fun onDestroy() {
        db.dropAll()
        db.close()
        Log.d("Destroy", "Database should have dropped everything")
        super.onDestroy()
    }

}


