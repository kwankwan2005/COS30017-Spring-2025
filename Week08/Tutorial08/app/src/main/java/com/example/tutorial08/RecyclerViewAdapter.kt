package com.example.lecture07

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecyclerListener
import com.example.tutorial08.ListItemDouble
import com.example.tutorial08.R
import org.w3c.dom.Text

class RecyclerViewAdapter (private val mList: List<ListItemDouble>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderClass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.re_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val Elements = mList[position]
        holder.textView.setText(Elements.columnA)
        holder.textView2.setText(Elements.columnB)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolderClass(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = ItemView.findViewById(R.id.txtLine)
        val textView2: TextView = ItemView.findViewById(R.id.txtLine2)
    }

}