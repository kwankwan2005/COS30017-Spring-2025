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

    private val _allTransactions = mutableListOf<Transaction>()
    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    fun loadAllTransactions(context: Context) {
        val dao = AppDatabase.getInstance(context).transactionDao()
        viewModelScope.launch {
            val list = dao.getAllTransactions()
            _allTransactions.clear()
            _allTransactions.addAll(list)
            _transactions.postValue(list)
        }
    }

    fun filterTransactions(isIncome: Boolean?) {
        val filtered = when (isIncome) {
            null -> _allTransactions
            true -> _allTransactions.filter { it.isIncome }
            false -> _allTransactions.filter { !it.isIncome }
        }
        _transactions.postValue(filtered)
    }
}
