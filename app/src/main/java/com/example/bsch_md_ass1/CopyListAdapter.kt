package com.example.bsch_md_ass1

import android.content.Context
import android.opengl.Visibility
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.bsch_md_ass1.db.BookCopy
import com.example.bsch_md_ass1.db.Loaner

open class CopyListAdapter(context : Context, copyList : ArrayList<BookCopy>, val currentUser : Loaner) : CustomElementsAdapter<BookCopy>(context, R.layout.book_copy, copyList) {

    override fun View.onCreateItemView(item : BookCopy) {

        //Book info
        findViewById<TextView>(R.id.loanID).text = item.ID.toString()
        findViewById<TextView>(R.id.loanTitle).text = item.book.title
        findViewById<TextView>(R.id.loanAuthor).text = item.book.author
        findViewById<TextView>(R.id.loanGenre).text = item.book.genre

        //Current loaner info
        findViewById<TextView>(R.id.loanLoanerName).text = item.loaner?.fullname
        findViewById<TextView>(R.id.loanLoanerID).text = item.loaner?.ID.toString()

        //Button interaction
        val button = findViewById<Button>(R.id.loanButton)

        if (item.loaner == null) {
            button.text = context.getString(R.string.loan_button_label)
            button.setOnClickListener { item.interact() }
        } else if (item.loaner!!.ID == currentUser.ID) {
            button.text = context.getString(R.string.loan_return_button_label)
            button.setOnClickListener { item.interact() }
        } else {
            button.isEnabled = false
        }
    }

    open fun BookCopy.interact() {}
}
