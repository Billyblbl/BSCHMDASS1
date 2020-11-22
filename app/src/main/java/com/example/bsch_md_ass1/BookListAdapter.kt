package com.example.bsch_md_ass1

import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.bsch_md_ass1.db.Book

class BookListAdapter(context : Context, bookList : ArrayList<Book>) : CustomElementsAdapter<Book>(context, R.layout.book, bookList) {

    override fun View.onCreateItemView(item: Book) {
        findViewById<TextView>(R.id.bookID).text = item.id.toString()
        findViewById<TextView>(R.id.bookTitle).text = item.title
        findViewById<TextView>(R.id.bookAuthor).text = item.author
        findViewById<TextView>(R.id.bookGenre).text = item.genre
        findViewById<TextView>(R.id.bookCopies).text = item.copiesAmount.toString()
        findViewById<TextView>(R.id.bookLoaned).text = item.loanedCopies.toString()
        findViewById<TextView>(R.id.bookAvailables).text = item.availables.toString()
    }

}
