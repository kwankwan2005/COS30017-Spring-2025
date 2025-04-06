package com.example.assignment3.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3.R
import com.example.assignment3.adapter.TransactionAdapter
import com.example.assignment3.viewmodel.TransactionListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TransactionListActivity : AppCompatActivity(), View.OnClickListener {
    // UI elements
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var btnFilter: FloatingActionButton
    private lateinit var btnBack: ImageButton
    private lateinit var txtTitle: TextView

    // ViewModel for transaction list
    private val viewModel: TransactionListViewModel by viewModels()

    // Activity result launcher for handling results from other activities
    private val transactionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Reload all transactions
            viewModel.loadAllTransactions(applicationContext)
            Log.d("TransactionListActivity", "Transaction activity result received, transactions reloaded")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_list)

        // Bind UI elements
        recyclerView = findViewById(R.id.rvTransactions)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAdd = findViewById(R.id.btnAdd)
        btnFilter = findViewById(R.id.btnFilter)
        btnBack = findViewById(R.id.btnBack)
        txtTitle = findViewById(R.id.txtTitle)

        // Set click listeners for buttons
        btnAdd.setOnClickListener(this)
        btnFilter.setOnClickListener(this)
        btnBack.setOnClickListener(this)

        // Observe transactions from ViewModel and update UI
        viewModel.transactions.observe(this) { transactions ->
            // Set up RecyclerView with the transactions
            val adapter = TransactionAdapter(transactions) { transaction ->
                // Launch transaction detail screen when a transaction is clicked
                val intent = Intent(this, TransactionDetailActivity::class.java)
                intent.putExtra("transaction_id", transaction.id)
                transactionLauncher.launch(intent)
                Log.d("TransactionListActivity", "Transaction clicked: ${transaction.id}, launching TransactionDetailActivity")
            }
            recyclerView.adapter = adapter
            Log.d("TransactionListActivity", "Transactions updated: ${transactions.size} transactions loaded")
        }

        // Load all transactions initially
        viewModel.loadAllTransactions(applicationContext)
        Log.d("TransactionListActivity", "Activity created and initial transactions loaded")
    }

    // On-click listeners
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {
                // Launch add transaction screen to add a new transaction
                val intent = Intent(this, AddEditTransactionActivity::class.java)
                transactionLauncher.launch(intent)
                Log.d("TransactionListActivity", "Add button clicked, launching AddEditTransactionActivity")
            }
            R.id.btnFilter -> {
                // Show filter menu to filter transactions
                showFilterMenu()
                Log.d("TransactionListActivity", "Filter button clicked, showing filter menu")
            }
            R.id.btnBack -> {
                // Finish activity and return to previous screen
                setResult(RESULT_OK)
                finish()
                Log.d("TransactionListActivity", "Back button clicked, finishing activity")
            }
        }
    }

    // Show filter menu to filter transactions by type
    private fun showFilterMenu() {
        val popup = PopupMenu(this, btnFilter)
        popup.menuInflater.inflate(R.menu.menu_filter, popup.menu)

        // Set on-click listener for filtering
        popup.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.filter_all -> {
                    viewModel.filterTransactions(null)
                    Log.d("TransactionListActivity", "Filter selected: All transactions")
                }
                R.id.filter_income -> {
                    viewModel.filterTransactions(true)
                    Log.d("TransactionListActivity", "Filter selected: Income transactions")
                }
                R.id.filter_expense -> {
                    viewModel.filterTransactions(false)
                    Log.d("TransactionListActivity", "Filter selected: Expense transactions")
                }
            }
            true
        }
        popup.show()
        Log.d("TransactionListActivity", "Filter menu shown")
    }
}