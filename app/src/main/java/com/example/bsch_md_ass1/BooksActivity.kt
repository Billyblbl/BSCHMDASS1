package com.example.bsch_md_ass1

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.bsch_md_ass1.db.Book
import com.example.bsch_md_ass1.db.BookCopy
import com.example.bsch_md_ass1.db.DBController
import com.example.bsch_md_ass1.db.Loaner
import java.util.ArrayList

class BooksActivity : AppCompatActivity() {

    lateinit var db: DBController

    val bookList = ArrayList<Book>()
    lateinit var bookListAdapter : BookListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.books_activity)

        //TODO correctly manage error instead of this dirty englobing try statement
        try {
            //GUI
            val bookListView = findViewById<ListView>(R.id.bookList)
            bookListAdapter = BookListAdapter(this, R.layout.book, bookList)
            bookListView.adapter = bookListAdapter

            //Database
            db = DBController(this, "library.db", null, 1)

            db.onCreate(db.writableDatabase)

            //Test values

            for (book in arrayOf(
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "A Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "Another Book", author = "Hector", genre = "Sci-Fi"),
                    Book(title = "And another", author = "Philippe", genre = "Fantasy")
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
            updateBookList()
        } catch (e : Exception) {

        }

    }

    override fun onResume() {
        super.onResume()
        updateBookList()
    }

    fun updateBookList() {
//        bookListAdapter.clear()
//        bookListAdapter.addAll(db.allBooks)
        bookList.clear()
        bookList.addAll(db.allBooks)
        bookListAdapter.notifyDataSetChanged()
        Log.d("bookListAdapter.count", bookListAdapter.count.toString())
        /*
        for (i in 0 until bookListAdapter.count) {
            val book = bookListAdapter.getItem(i)
            Log.d("Book", "{ id = ${book?.id}, title = ${book?.title}, author = ${book?.author}, genre = ${book?.genre} }")
        }
         */

        for (book in bookList) {
            Log.d("Book", "{ id = ${book?.id}, " +
                    "title = ${book?.title}, " +
                    "author = ${book?.author}, " +
                    "genre = ${book?.genre}, " +
                    "copies = ${book?.copiesAmount}, " +
                    "loaned = ${book?.loanedCopies}, " +
                    "availables = ${book?.availables}, " +
                    "}")
        }
    }

    //TODO for testing purposes, remove for delivery
    override fun onDestroy() {
        db.dropAll()
        db.close()
        Log.d("Destroy", "Database should have dropped everything")
        super.onDestroy()
    }

}


