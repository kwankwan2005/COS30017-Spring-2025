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

    // DAO for transactions
    private val dao = AppDatabase.getInstance(application).transactionDao()

    // LiveData for the list of transactions
    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    // LiveData for total income
    private val _totalIncome = MutableLiveData<Double>()
    val totalIncome: LiveData<Double> = _totalIncome

    // LiveData for total expense
    private val _totalExpense = MutableLiveData<Double>()
    val totalExpense: LiveData<Double> = _totalExpense

    // Function to load data for the current day
    fun loadTodayData() {
        viewModelScope.launch { // Launch in a coroutine -- to call DAO
            val now = System.currentTimeMillis()

            // Insert sample transactions if the database is empty
            if (dao.getAllTransactions().isEmpty()) {
                dao.insertTransaction(Transaction(0, "Salary from MoMo", 2500.0, true, now, "Salary"))
                dao.insertTransaction(Transaction(0, "Groceries", 89.99, false, now, "Shopping"))
                dao.insertTransaction(Transaction(0, "Coffee", 4.50, false, now, "Food & Drink"))
            }

            // Calculate the start and end of the current month
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

            // Update live data with total income and expense for the current month
            _totalIncome.postValue(dao.getTotalIncomeInPeriod(monthStart, monthEnd) ?: 0.0)
            _totalExpense.postValue(dao.getTotalExpenseInPeriod(monthStart, monthEnd) ?: 0.0)

            // Update live data with the most recent transactions
            _transactions.postValue(dao.getRecentTransactions(3))
        }
    }
}