package com.example.assignment3.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.assignment3.R
import com.example.assignment3.util.CategoryStyleMapper
import com.example.assignment3.viewmodel.TransactionDetailViewModel
import java.text.SimpleDateFormat
import java.util.*

class TransactionDetailActivity : AppCompatActivity(), View.OnClickListener {

    // UI elements
    private lateinit var txtAmount: TextView
    private lateinit var txtDate: TextView
    private lateinit var txtTransaction: TextView
    private lateinit var txtCategory: TextView
    private lateinit var txtCategorySummary: TextView
    private lateinit var txtCategorySummaryTitle: TextView
    private lateinit var imgCategoryIcon: ImageView
    private lateinit var imgCategorySummaryIcon: ImageView
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private lateinit var btnBack: ImageButton

    private var transactionId: Int = -1

    // ViewModel for transaction details
    private val viewModel: TransactionDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)

        // Bind UI elements
        txtAmount = findViewById(R.id.txtAmount)
        txtDate = findViewById(R.id.txtDate)
        txtTransaction = findViewById(R.id.txtTransaction)
        txtCategory = findViewById(R.id.txtCategory)
        txtCategorySummary = findViewById(R.id.txtCategorySummary)
        txtCategorySummaryTitle = findViewById(R.id.txtCategorySummaryTitle)
        imgCategoryIcon = findViewById(R.id.imgCategoryIcon)
        imgCategorySummaryIcon = findViewById(R.id.imgCategorySummaryIcon)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)
        btnBack = findViewById(R.id.btnBack)

        // Set click listeners for buttons
        btnEdit.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        btnBack.setOnClickListener(this)

        // Get transaction ID from intent
        transactionId = intent.getIntExtra("transaction_id", -1)
        if (transactionId == -1) {
            // If transaction ID is invalid, show a toast and finish the activity
            Toast.makeText(this, "Transaction not found", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
            return
        }

        // Load transaction details
        viewModel.loadTransaction(applicationContext, transactionId)
        Log.d("TransactionDetailActivity", "Transaction ID: $transactionId, loading transaction details")

        // Observe transaction details and update UI
        viewModel.transaction.observe(this, Observer { transaction ->
            transaction?.let {
                // Update UI with transaction details
                val isIncome = it.isIncome
                txtAmount.text = (if (isIncome) "+$" else "-$") + String.format("%.2f", it.amount)
                txtAmount.setTextColor(getColor(if (isIncome) R.color.green else R.color.red))
                txtTransaction.text = it.title

                val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date(it.date))
                val overviewText = "${it.category} Overview: $monthName"
                txtCategorySummaryTitle.text = overviewText
                txtCategory.text = it.category

                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                txtDate.text = dateFormat.format(Date(it.date))

                val style = CategoryStyleMapper.getStyle(it.category)
                imgCategoryIcon.setImageResource(style.iconResId)
                imgCategorySummaryIcon.setImageResource(style.iconResId)
                imgCategoryIcon.setColorFilter(getColor(style.fgColorResId))
                imgCategorySummaryIcon.setColorFilter(getColor(style.fgColorResId))

                // Set arguments for embedded chart fragment
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = it.date
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val monthStart = calendar.timeInMillis
                calendar.add(Calendar.MONTH, 1)
                val monthEnd = calendar.timeInMillis

                // Load chart data for the specified category and date range
                val chartFragment = supportFragmentManager.findFragmentById(R.id.chartContainer) as ChartFragment
                chartFragment.loadChartData(applicationContext, it.category, monthStart, monthEnd)
                Log.d("TransactionDetailActivity", "Chart data loaded for category: ${it.category}, date range: $monthStart to $monthEnd")
            }
        })

        // Observe category summary and update UI
        viewModel.categorySummary.observe(this, Observer { summary ->
            txtCategorySummary.text = summary
            Log.d("TransactionDetailActivity", "Category summary updated: $summary")
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnEdit -> {
                // Launch edit activity
                val intent = Intent(this, AddEditTransactionActivity::class.java)
                intent.putExtra("transaction_id", transactionId)
                editLauncher.launch(intent)
                Log.d("TransactionDetailActivity", "Edit button clicked, launching AddEditTransactionActivity")
            }
            R.id.btnDelete -> {
                // Show confirmation dialog before deleting transaction
                viewModel.transaction.value?.let { transaction ->
                    AlertDialog.Builder(this)
                        .setTitle("Delete transaction")
                        .setMessage("Are you sure you want to delete this transaction?")
                        .setPositiveButton("Delete") { _, _ ->
                            // Delete transaction and finish activity
                            viewModel.deleteTransaction(applicationContext, transaction) {
                                Toast.makeText(this, "Transaction deleted", Toast.LENGTH_SHORT).show()
                                setResult(RESULT_OK)
                                finish()
                                Log.d("TransactionDetailActivity", "Transaction deleted: ${transaction.id}")
                            }
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            }
            R.id.btnBack -> {
                // Finish activity and return to previous screen
                setResult(RESULT_OK)
                finish()
                Log.d("TransactionDetailActivity", "Back button clicked, finishing activity")
            }
        }
    }

    // Activity result launcher for editing transaction
    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Reload transaction details
            viewModel.loadTransaction(applicationContext, transactionId)
            Log.d("TransactionDetailActivity", "Edit activity result received, transaction reloaded")
        }
    }
}