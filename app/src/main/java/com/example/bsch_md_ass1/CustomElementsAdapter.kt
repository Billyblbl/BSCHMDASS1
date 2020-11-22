package com.example.bsch_md_ass1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

open class CustomElementsAdapter<T>(context : Context, private val resource : Int, list : ArrayList<T>) : ArrayAdapter<T>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)!!
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        view?.onCreateItemView(item)
        return view
    }

    open fun View.onCreateItemView(item : T) {}

}