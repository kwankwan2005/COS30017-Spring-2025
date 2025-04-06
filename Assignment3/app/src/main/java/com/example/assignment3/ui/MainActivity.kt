package com.example.assignment3.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3.R
import com.example.assignment3.adapter.TransactionAdapter
import com.example.assignment3.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), View.OnClickListener {
    // Elements setup
    private lateinit var txtTotalIncome: TextView
    private lateinit var txtTotalExpense: TextView
    private lateinit var txtBalance: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var btnViewAll: Button

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind views
        txtTotalIncome = findViewById(R.id.txtIncomeAmount)
        txtTotalExpense = findViewById(R.id.txtExpenseAmount)
        txtBalance = findViewById(R.id.txtBalance)
        recyclerView = findViewById(R.id.rvTransactions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        btnAdd = findViewById(R.id.btnAdd)
        btnViewAll = findViewById(R.id.btnViewAll)

        // Set button click listeners
        btnAdd.setOnClickListener(this)
        btnViewAll.setOnClickListener(this)

        // Observe ViewModel to update UI
        viewModel.totalIncome.observe(this) { income ->
            txtTotalIncome.text = "$${String.format("%.2f", income)}"
            val expense = viewModel.totalExpense.value ?: 0.0
            txtBalance.text = "$${String.format("%.2f", income - expense)}"
            Log.d("MainActivity", "Total income updated: $income")
        }

        viewModel.totalExpense.observe(this) { expense ->
            txtTotalExpense.text = "$${String.format("%.2f", expense)}"
            val income = viewModel.totalIncome.value ?: 0.0
            txtBalance.text = "$${String.format("%.2f", income - expense)}"
            Log.d("MainActivity", "Total expense updated: $expense")
        }

        // Observe transactions to update RecyclerView
        viewModel.transactions.observe(this) { transactions ->
            // Set up RecyclerView with the transactions
            // For each transaction, set up the on click event
            val adapter = TransactionAdapter(transactions) { transaction ->
                val intent = Intent(this, TransactionDetailActivity::class.java)
                intent.putExtra("transaction_id", transaction.id)
                detailLauncher.launch(intent)
            }
            recyclerView.adapter = adapter
            Log.d("MainActivity", "Transactions updated: ${transactions.size} transactions loaded")
        }

        // Load initial data
        viewModel.loadTodayData()
        Log.d("MainActivity", "Activity created and initial data loaded")
    }

    // On-click handler
    // Launch activity and subscribe for result
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {
                Log.d("MainActivity", "Add button clicked")
                val intent = Intent(this, AddEditTransactionActivity::class.java)
                addEditLauncher.launch(intent)
            }
            R.id.btnViewAll -> {
                Log.d("MainActivity", "View all button clicked")
                val intent = Intent(this, TransactionListActivity::class.java)
                viewAllLauncher.launch(intent)
            }
        }
    }

    // Register activity result launchers
    private val detailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.loadTodayData()
        Log.d("MainActivity", "Detail activity result received")
    }

    private val addEditLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.loadTodayData()
            Log.d("MainActivity", "Add/Edit activity result received and data reloaded")
        }
    }

    private val viewAllLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.loadTodayData()
            Log.d("MainActivity", "View All activity result received and data reloaded")
        }
    }
}