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

class TransactionAdapter(private val mList: List<Transaction>, private val onItemClick: (Transaction) -> Unit) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val transaction = mList[position]
        val context = holder.itemView.context

        holder.txtCategory.text = transaction.category
        holder.txtTitle.text = transaction.title

        val isIncome = transaction.isIncome
        val amountText = (if (isIncome) "+" else "-") + String.format("$%.2f", transaction.amount)
        holder.txtAmount.text = amountText
        holder.txtAmount.setTextColor(
            ContextCompat.getColor(context, if (isIncome) R.color.green else R.color.red)
        )

        val style = CategoryStyleMapper.getStyle(transaction.category)

        holder.imgIcon.setImageResource(style.iconResId)
        holder.imgIcon.setColorFilter(ContextCompat.getColor(context, style.fgColorResId))

        val bgDrawable = ContextCompat.getDrawable(context, R.drawable.bg_icon_circle)?.mutate()
        bgDrawable?.setTint(ContextCompat.getColor(context, style.bgColorResId))
        holder.iconContainer.background = bgDrawable

        holder.itemView.setOnClickListener {
            onItemClick(transaction)
        }
    }

    override fun getItemCount(): Int = mList.size

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCategory: TextView = itemView.findViewById(R.id.txtCategory)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTransactionName)
        val txtAmount: TextView = itemView.findViewById(R.id.txtAmount)
        val imgIcon: ImageView = itemView.findViewById(R.id.imgCategoryIcon)
        val iconContainer: View = itemView.findViewById(R.id.iconContainer) // Make sure this is the FrameLayout or container for the icon
    }
}
