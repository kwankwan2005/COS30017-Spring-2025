package com.example.assignment3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3.R
import com.example.assignment3.data.model.Transaction
import com.example.assignment3.util.CategoryStyleMapper

// Adapter class for displaying a list of transactions in a RecyclerView
class TransactionAdapter(
    private val mList: List<Transaction>, // List of transactions to display
    private val onItemClick: (Transaction) -> Unit // Click listener for each item
) : RecyclerView.Adapter<TransactionAdapter.ViewHolderClass>() {

    // Creates and returns a ViewHolder object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolderClass(itemView)
    }

    // Binds data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val transaction = mList[position]
        val context = holder.itemView.context

        // Set the category and title text
        holder.txtCategory.text = transaction.category
        holder.txtTitle.text = transaction.title

        // Determine if the transaction is income or expense and set the amount text and color
        val isIncome = transaction.isIncome
        val amountText = (if (isIncome) "+" else "-") + String.format("$%.2f", transaction.amount)
        holder.txtAmount.text = amountText
        holder.txtAmount.setTextColor(
            ContextCompat.getColor(context, if (isIncome) R.color.green else R.color.red)
        )

        // Get the style for the category and set the icon and background colors
        val style = CategoryStyleMapper.getStyle(transaction.category)
        holder.imgIcon.setImageResource(style.iconResId)
        holder.imgIcon.setColorFilter(ContextCompat.getColor(context, style.fgColorResId))

        // Set the background color of the icon container
        val bgDrawable = ContextCompat.getDrawable(context, R.drawable.bg_icon_circle)?.mutate()
        bgDrawable?.setTint(ContextCompat.getColor(context, style.bgColorResId))
        holder.iconContainer.background = bgDrawable

        // Set the click listener for the item
        holder.itemView.setOnClickListener {
            onItemClick(transaction)
        }
    }

    // Returns the total number of items
    override fun getItemCount(): Int = mList.size

    // ViewHolder class to hold the views for each item
    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCategory: TextView = itemView.findViewById(R.id.txtCategory) // TextView for category
        val txtTitle: TextView = itemView.findViewById(R.id.txtTransactionName) // TextView for title
        val txtAmount: TextView = itemView.findViewById(R.id.txtAmount) // TextView for amount
        val imgIcon: ImageView = itemView.findViewById(R.id.imgCategoryIcon) // ImageView for category icon
        val iconContainer: View = itemView.findViewById(R.id.iconContainer) // Container for the icon
    }
}