package com.example.assignment3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.assignment3.data.database.AppDatabase
import com.example.assignment3.data.model.Transaction
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).transactionDao()

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    private val _totalIncome = MutableLiveData<Double>()
    val totalIncome: LiveData<Double> = _totalIncome

    private val _totalExpense = MutableLiveData<Double>()
    val totalExpense: LiveData<Double> = _totalExpense

    fun loadTodayData() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            if (dao.getAllTransactions().isEmpty()) {
                dao.insertTransaction(Transaction(0, "Salary from MoMo", 2500.0, true, now, "Salary"))
                dao.insertTransaction(Transaction(0, "Groceries", 89.99, false, now, "Shopping"))
                dao.insertTransaction(Transaction(0, "Coffee", 4.50, false, now, "Food & Drink"))
            }

            val monthStart = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            val monthEnd = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                add(Calendar.MONTH, 1)
            }.timeInMillis

            _totalIncome.postValue(dao.getTotalIncomeInPeriod(monthStart, monthEnd) ?: 0.0)
            _totalExpense.postValue(dao.getTotalExpenseInPeriod(monthStart, monthEnd) ?: 0.0)
            _transactions.postValue(dao.getRecentTransactions(3))
        }
    }
}
