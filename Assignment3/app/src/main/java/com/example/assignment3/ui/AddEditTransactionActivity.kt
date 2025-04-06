package com.example.assignment3.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.assignment3.R
import com.example.assignment3.data.database.AppDatabase
import com.example.assignment3.data.model.Transaction
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddEditTransactionActivity : AppCompatActivity(), View.OnClickListener {

    private var transactionId: Int = -1
    private var isEditing = false

    private lateinit var txtTitle: EditText
    private lateinit var txtAmount: EditText
    private lateinit var btnIncome: Button
    private lateinit var btnExpense: Button
    private lateinit var txtDate: TextView
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button
    private lateinit var btnBack: ImageButton

    private var isIncome = true
    private var selectedDate: Long = System.currentTimeMillis()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_edit)

        transactionId = intent.getIntExtra("transaction_id", -1)
        isEditing = transactionId != -1

        txtTitle = findViewById(R.id.txtTitle)
        txtAmount = findViewById(R.id.txtAmount)
        btnIncome = findViewById(R.id.btnIncome)
        btnExpense = findViewById(R.id.btnExpense)
        txtDate = findViewById(R.id.txtDate)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnCancel = findViewById(R.id.btnCancel)
        btnBack = findViewById(R.id.btnBack)

        btnIncome.setOnClickListener(this)
        btnExpense.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
        btnBack.setOnClickListener(this)

        setupForm()

        if (isEditing) {
            loadTransaction()
        }

        txtDate.text = dateFormat.format(Date(selectedDate))
        txtDate.setOnClickListener {
            val calendar = Calendar.getInstance().apply { timeInMillis = selectedDate }
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    selectedDate = cal.timeInMillis
                    txtDate.text = dateFormat.format(cal.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }

    private fun setupForm() {
        updateCategoryOptions()
        updateTypeButtons()
    }

    private fun updateCategoryOptions() {
        val categories = if (isIncome) {
            listOf("Salary", "Gift", "Bonus")
        } else {
            listOf("Shopping", "Bills", "Food & Drink", "Relatives")
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }

    private fun updateTypeButtons() {
        if (isIncome) {
            btnIncome.setBackgroundResource(R.drawable.bg_button_income)
            btnIncome.setTextColor(ContextCompat.getColor(this, R.color.green))
            btnIncome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_down, 0, 0, 0)
            btnIncome.compoundDrawableTintList = ContextCompat.getColorStateList(this, R.color.green)

            btnExpense.setBackgroundResource(R.drawable.bg_button_transaction_default)
            btnExpense.setTextColor(ContextCompat.getColor(this, R.color.gray))
            btnExpense.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_up, 0, 0, 0)
            btnExpense.compoundDrawableTintList = ContextCompat.getColorStateList(this, R.color.gray)
        } else {
            btnExpense.setBackgroundResource(R.drawable.bg_button_expense)
            btnExpense.setTextColor(ContextCompat.getColor(this, R.color.red))
            btnExpense.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_up, 0, 0, 0)
            btnExpense.compoundDrawableTintList = ContextCompat.getColorStateList(this, R.color.red)

            btnIncome.setBackgroundResource(R.drawable.bg_button_transaction_default)
            btnIncome.setTextColor(ContextCompat.getColor(this, R.color.gray))
            btnIncome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_down, 0, 0, 0)
            btnIncome.compoundDrawableTintList = ContextCompat.getColorStateList(this, R.color.gray)
        }
    }

    private fun loadTransaction() {
        val dao = AppDatabase.getInstance(applicationContext).transactionDao()
        lifecycleScope.launch {
            val transaction = dao.getTransactionById(transactionId)
            transaction?.let {
                txtTitle.setText(it.title)
                txtAmount.setText(it.amount.toString())
                isIncome = it.isIncome
                selectedDate = it.date
                updateCategoryOptions()
                val categoryIndex = (spinnerCategory.adapter as ArrayAdapter<String>).getPosition(it.category)
                spinnerCategory.setSelection(categoryIndex)
                updateTypeButtons()
                txtDate.text = dateFormat.format(it.date)
                btnSubmit.text = "Update"
            }
        }
    }

    private fun showDismissibleSnackbar(message: String) {
        Snackbar.make(btnSubmit, message, Snackbar.LENGTH_LONG)
            .setAction("DISMISS") {}
            .show()
    }

    private fun handleSubmit() {
        val title = txtTitle.text.toString().trim()
        val amountText = txtAmount.text.toString().trim()

        if (title.isEmpty() || amountText.isEmpty()) {
            showDismissibleSnackbar("Please fill in all fields.")
            return
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            showDismissibleSnackbar("Enter a valid amount.")
            return
        }

        val category = spinnerCategory.selectedItem?.toString() ?: ""
        if (category.isBlank()) {
            showDismissibleSnackbar("Please select a category.")
            return
        }

        val dao = AppDatabase.getInstance(applicationContext).transactionDao()
        val transaction = Transaction(
            id = if (isEditing) transactionId else 0,
            title = title,
            amount = amount,
            isIncome = isIncome,
            date = selectedDate,
            category = category
        )

        lifecycleScope.launch {
            if (isEditing) {
                dao.updateTransaction(transaction)
                showDismissibleSnackbar("Transaction updated.")
            } else {
                dao.insertTransaction(transaction)
                showDismissibleSnackbar("Transaction added.")
            }
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnIncome -> {
                isIncome = true
                updateTypeButtons()
                updateCategoryOptions()
            }
            R.id.btnExpense -> {
                isIncome = false
                updateTypeButtons()
                updateCategoryOptions()
            }
            R.id.btnSubmit -> {
                handleSubmit()
            }
            R.id.btnCancel, R.id.btnBack -> {
                finish()
            }
        }
    }
}