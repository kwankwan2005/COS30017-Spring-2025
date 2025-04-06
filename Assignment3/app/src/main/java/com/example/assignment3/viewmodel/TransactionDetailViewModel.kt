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

    private val _transaction = MutableLiveData<Transaction?>()
    val transaction: LiveData<Transaction?> = _transaction

    private val _categorySummary = MutableLiveData<String>()
    val categorySummary: LiveData<String> = _categorySummary

    fun loadTransaction(context: Context, transactionId: Int) {
        val dao = AppDatabase.getInstance(context).transactionDao()

        viewModelScope.launch {
            val transaction = dao.getTransactionById(transactionId)
            _transaction.postValue(transaction)

            transaction?.let {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = it.date
                    set(Calendar.DAY_OF_MONTH, 1)
                }
                val monthStart = calendar.timeInMillis
                calendar.add(Calendar.MONTH, 1)
                val monthEnd = calendar.timeInMillis

                val total = dao.getMonthlyCategorySpending(
                    category = it.category,
                    start = monthStart,
                    end = monthEnd
                ) ?: 0.0

                val formatted = String.format("%.2f", total)

                if(!transaction.isIncome) {
                    _categorySummary.postValue("This month, you have spent $$formatted on ${it.category}. Be careful with your spending!")
                }
                else {
                    _categorySummary.postValue("This month, you have received $$formatted on ${it.category}. Keep it up!")
                }
            }
        }
    }

    fun deleteTransaction(context: Context, transaction: Transaction, onDeleted: () -> Unit) {
        val dao = AppDatabase.getInstance(context).transactionDao()
        viewModelScope.launch {
            dao.deleteTransaction(transaction)
            onDeleted()
        }
    }
}
