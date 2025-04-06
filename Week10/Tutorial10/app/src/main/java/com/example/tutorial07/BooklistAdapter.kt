package com.example.tutorial07

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class BooklistAdapter (private val mList: List<Book>): RecyclerView.Adapter<BooklistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.bind(item)
    }

    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val image = v.findViewById<ImageView>(R.id.cover)
        val title = v.findViewById<TextView>(R.id.title)
        val rating = v.findViewById<RatingBar>(R.id.rating)

        fun bind(item: Book) {
            title.text = item.title
            rating.rating = item.rating

            Picasso.get()
                .load(item.image)
                .placeholder(android.R.drawable.picture_frame)
                .error(android.R.drawable.alert_light_frame)
                .into(image)
        }
    }
}