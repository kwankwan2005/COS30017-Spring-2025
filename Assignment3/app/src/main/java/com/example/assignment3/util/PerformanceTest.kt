package com.example.assignment3.util

import android.content.Context
import android.util.Log
import com.example.assignment3.data.database.AppDatabase
import com.example.assignment3.data.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis
import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PerformanceTest {

    suspend fun runInsertPerformanceTest(context: Context) = withContext(Dispatchers.IO) {
        val dao = AppDatabase.getInstance(context).transactionDao()
        val sampleTransactions = List(100) { i ->
            Transaction(
                id = 0,
                title = "Test #$i",
                amount = (1..1000).random().toDouble(),
                isIncome = i % 2 == 0,
                date = System.currentTimeMillis(),
                category = if (i % 2 == 0) "Salary" else "Shopping"
            )
        }

        // Reset all transactions for testing
        dao.deleteAllTransactions()
        File(context.filesDir, "transactions.json").delete()

        // Add each transaction for Room
        val roomTime = measureTimeMillis {
            sampleTransactions.forEach { dao.insertTransaction(it) }
        }

        // Add each transaction for file
        val fileTime = measureTimeMillis {
            val file = File(context.filesDir, "transactions.json")
            val json = Gson().toJson(sampleTransactions)
            file.writeText(json)
        }

        // Performance Test
        Log.d("PerformanceTest", "Room insert: ${roomTime}ms")
        Log.d("PerformanceTest", "File write: ${fileTime}ms")
    }

    // Read performance test
    suspend fun runReadPerformanceTest(context: Context) = withContext(Dispatchers.IO) {
        val dao = AppDatabase.getInstance(context).transactionDao()

        val roomTime = measureTimeMillis {
            dao.getAllTransactions()
        }

        val fileTime = measureTimeMillis {
            val file = File(context.filesDir, "transactions.json")
            val json = file.readText()
            val type = object : TypeToken<List<Transaction>>() {}.type
            Gson().fromJson<List<Transaction>>(json, type)
        }

        Log.d("PerformanceTest", "Room read: ${roomTime}ms")
        Log.d("PerformanceTest", "File read: ${fileTime}ms")
    }

    suspend fun runScalabilityTest(context: Context) = withContext(Dispatchers.IO) {
        val dao = AppDatabase.getInstance(context).transactionDao()
        val sizes = listOf(100, 1000, 10000)

        for (size in sizes) {
            val transactions = List(size) { i ->
                Transaction(
                    id = 0,
                    title = "Test $i",
                    amount = (1..500).random().toDouble(),
                    isIncome = i % 2 == 0,
                    date = System.currentTimeMillis(),
                    category = if (i % 2 == 0) "Salary" else "Shopping"
                )
            }

            dao.deleteAllTransactions()
            File(context.filesDir, "scalability_test.json").delete()

            val roomInsert = measureTimeMillis {
                transactions.forEach { dao.insertTransaction(it) }
            }

            val fileWrite = measureTimeMillis {
                val file = File(context.filesDir, "scalability_test.json")
                val json = Gson().toJson(transactions)
                file.writeText(json)
            }

            val roomRead = measureTimeMillis {
                dao.getAllTransactions()
            }

            val fileRead = measureTimeMillis {
                val file = File(context.filesDir, "scalability_test.json")
                val json = file.readText()
                val type = object : TypeToken<List<Transaction>>() {}.type
                Gson().fromJson<List<Transaction>>(json, type)
            }

            Log.d("ScalabilityTest", "Size: $size | Room Insert: ${roomInsert}ms | File Write: ${fileWrite}ms")
            Log.d("ScalabilityTest", "Size: $size | Room Read: ${roomRead}ms | File Read: ${fileRead}ms")
        }
    }

}
