package com.example.bsch_md_ass1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.bsch_md_ass1.db.Book

class BookListAdapter(context : Context, private val resource : Int, bookList : ArrayList<Book>) : ArrayAdapter<Book>(context, 0, bookList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val book = getItem(position)!!
//        Log.d("convertView Before", convertView.toString())
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
//        Log.d("convertView After", view.toString())

        if (view != null) {
            view.findViewById<TextView>(R.id.bookID).text = book.id.toString()
            view.findViewById<TextView>(R.id.bookTitle).text = book.title
            view.findViewById<TextView>(R.id.bookAuthor).text = book.author
            view.findViewById<TextView>(R.id.bookGenre).text = book.genre
            view.findViewById<TextView>(R.id.bookCopies).text = book.copiesAmount.toString()
            view.findViewById<TextView>(R.id.bookLoaned).text = book.loanedCopies.toString()
            view.findViewById<TextView>(R.id.bookAvailables).text = book.availables.toString()
        }
        return view
    }

}