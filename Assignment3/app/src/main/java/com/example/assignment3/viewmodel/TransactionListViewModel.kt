package com.example.assignment3.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment3.data.database.AppDatabase
import com.example.assignment3.data.model.Transaction
import kotlinx.coroutines.launch

class TransactionListViewModel : ViewModel() {

    // List to hold all transactions
    private val _allTransactions = mutableListOf<Transaction>()
    // LiveData to hold the filtered transactions
    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    // Load all transactions from the database
    fun loadAllTransactions(context: Context) {
        // Get the DAO instance
        val dao = AppDatabase.getInstance(context).transactionDao()
        // Launch a coroutine to perform database operations
        viewModelScope.launch {
            // Fetch all transactions from the database
            val list = dao.getAllTransactions()
            // Clear the current list and add all fetched transactions
            _allTransactions.clear()
            _allTransactions.addAll(list)
            // Post the fetched transactions to LiveData
            _transactions.postValue(list)
        }
    }

    // Filter transactions by type (income or expense)
    fun filterTransactions(isIncome: Boolean?) {
        // Filter the transactions based on the type
        val filtered = when (isIncome) {
            null -> _allTransactions // No filter, return all transactions
            true -> _allTransactions.filter { it.isIncome } // Filter income transactions
            false -> _allTransactions.filter { !it.isIncome } // Filter expense transactions
        }
        // Post the filtered transactions to LiveData
        _transactions.postValue(filtered)
    }
}