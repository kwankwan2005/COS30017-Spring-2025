package com.example.assignment3.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment3.data.database.AppDatabase
import com.example.assignment3.data.model.Transaction
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TransactionDetailViewModel : ViewModel() {
    // LiveData to hold the transaction details
    private val _transaction = MutableLiveData<Transaction?>()
    val transaction: LiveData<Transaction?> = _transaction

    // LiveData to hold the category summary
    private val _categorySummary = MutableLiveData<String>()
    val categorySummary: LiveData<String> = _categorySummary

    // Load transaction details by ID
    fun loadTransaction(context: Context, transactionId: Int) {
        val dao = AppDatabase.getInstance(context).transactionDao()

        // Launch a coroutine to perform database operations
        viewModelScope.launch {
            // Fetch the transaction by ID
            // Post the fetched transaction to LiveData
            val transaction = dao.getTransactionById(transactionId)
            _transaction.postValue(transaction)

            // If the transaction is not null, calculate the category summary
            transaction?.let {
                // Set the calendar to the start of the month of the transaction date
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = it.date
                    set(Calendar.DAY_OF_MONTH, 1)
                }
                val monthStart = calendar.timeInMillis
                // Move the calendar to the start of the next month
                calendar.add(Calendar.MONTH, 1)
                val monthEnd = calendar.timeInMillis

                // Fetch the total spending/earning for the category in the current month
                val total = dao.getMonthlyCategorySpending(
                    category = it.category,
                    start = monthStart,
                    end = monthEnd
                ) ?: 0.0

                // Format the total amount
                val formatted = String.format("%.2f", total)

                // Update the category summary based on whether the transaction is income or expense
                if (!transaction.isIncome) {
                    _categorySummary.postValue("This month, you have spent $$formatted on ${it.category}. Be careful with your spending!")
                } else {
                    _categorySummary.postValue("This month, you have received $$formatted on ${it.category}. Keep it up!")
                }
            }
        }
    }

    // Delete a transaction
    fun deleteTransaction(context: Context, transaction: Transaction, onDeleted: () -> Unit) {
        val dao = AppDatabase.getInstance(context).transactionDao()
        // Launch a coroutine to perform the delete operation
        viewModelScope.launch {
            dao.deleteTransaction(transaction)
            onDeleted()
        }
    }
}