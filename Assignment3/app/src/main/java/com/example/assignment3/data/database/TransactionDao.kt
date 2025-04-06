package com.example.assignment3.data.database

import androidx.room.*
import com.example.assignment3.data.model.Transaction

@Dao
interface TransactionDao {
    // Get a single transaction by ID
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): Transaction?

    // Get all transactions (latest first)
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    suspend fun getAllTransactions(): List<Transaction>

    // Get transactions by date
    @Query("SELECT * FROM transactions WHERE date BETWEEN :start AND :end ORDER BY date DESC, id DESC")
    suspend fun getTransactionsInRange(start: Long, end: Long): List<Transaction>

    // Get recent transactions
    @Query("SELECT * FROM transactions WHERE date ORDER BY date DESC, id DESC LIMIT :limit")
    suspend fun getRecentTransactions(limit: Int): List<Transaction>

    // Insert
    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    // Update
    @Update
    suspend fun updateTransaction(transaction: Transaction)

    // Delete
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)


    // Get total amount for a category in a month
    @Query("SELECT SUM(amount) FROM transactions WHERE category = :category AND date BETWEEN :start AND :end")
    suspend fun getMonthlyCategorySpending(category: String, start: Long, end: Long): Double?

    // Total income in a time range
    @Query("SELECT SUM(amount) FROM transactions WHERE isIncome = 1 AND date BETWEEN :start AND :end")
    suspend fun getTotalIncomeInPeriod(start: Long, end: Long): Double?

    // Total expenses in a time range
    @Query("SELECT SUM(amount) FROM transactions WHERE isIncome = 0 AND date BETWEEN :start AND :end")
    suspend fun getTotalExpenseInPeriod(start: Long, end: Long): Double?

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}