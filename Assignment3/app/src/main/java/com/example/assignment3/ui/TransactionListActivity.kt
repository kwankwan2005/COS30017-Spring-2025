package com.example.assignment3.ui

import android.content.Intent
import android.os.Bundle
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var btnFilter: FloatingActionButton
    private lateinit var btnBack: ImageButton
    private lateinit var txtTitle: TextView

    private val viewModel: TransactionListViewModel by viewModels()

    private val transactionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.loadAllTransactions(applicationContext)
            setResult(RESULT_OK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_list)

        recyclerView = findViewById(R.id.rvTransactions)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAdd = findViewById(R.id.btnAdd)
        btnFilter = findViewById(R.id.btnFilter)
        btnBack = findViewById(R.id.btnBack)
        txtTitle = findViewById(R.id.txtTitle)

        btnAdd.setOnClickListener(this)
        btnFilter.setOnClickListener(this)
        btnBack.setOnClickListener(this)

        viewModel.transactions.observe(this) { transactions ->
            val adapter = TransactionAdapter(transactions) { transaction ->
                val intent = Intent(this, TransactionDetailActivity::class.java)
                intent.putExtra("transaction_id", transaction.id)
                transactionLauncher.launch(intent)
            }
            recyclerView.adapter = adapter
        }

        viewModel.loadAllTransactions(applicationContext)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {
                val intent = Intent(this, AddEditTransactionActivity::class.java)
                transactionLauncher.launch(intent)
            }
            R.id.btnFilter -> {
                showFilterMenu()
            }
            R.id.btnBack -> {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun showFilterMenu() {
        val popup = PopupMenu(this, btnFilter)
        popup.menuInflater.inflate(R.menu.menu_filter, popup.menu)

        popup.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.filter_all -> viewModel.filterTransactions(null)
                R.id.filter_income -> viewModel.filterTransactions(true)
                R.id.filter_expense -> viewModel.filterTransactions(false)
            }
            true
        }
        popup.show()
    }
}
