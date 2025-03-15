package com.example.assignment2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text

class DetailRenting : AppCompatActivity(), View.OnClickListener {
    // Variables initiailize
    lateinit var btnBack: ImageButton
    lateinit var txtProductNameDetail: TextView
    lateinit var txtCurrentBalanceDetail: TextView

    lateinit var imgProductDetail: ImageView
    lateinit var txtInstrumentNameDetail: TextView
    lateinit var txtRentingPriceDetail: TextView

    lateinit var chipDetailsArea: ChipGroup
    lateinit var txtName: TextInputLayout
    lateinit var txtEmail: TextInputLayout
    lateinit var txtRentingPeriod: TextInputLayout

    lateinit var ratingBarDetails: RatingBar

    lateinit var btnCancelButton: Button
    lateinit var btnConfirmButton: Button

    lateinit var selectedAccessories: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.detail_renting_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind variables with elements
        btnBack = findViewById(R.id.btnBack)
        txtProductNameDetail = findViewById(R.id.txtProductNameDetail)
        txtCurrentBalanceDetail = findViewById(R.id.txtCurrentBalanceDetail)

        imgProductDetail = findViewById(R.id.imgProductDetail)
        txtInstrumentNameDetail = findViewById(R.id.txtInstrumentNameDetail)
        txtRentingPriceDetail = findViewById(R.id.txtRentingPriceDetail)

        chipDetailsArea = findViewById(R.id.chipDetailsArea)
        txtName = findViewById(R.id.txtName)
        txtEmail = findViewById(R.id.txtEmail)
        txtRentingPeriod = findViewById(R.id.txtRentingPeriod)

        ratingBarDetails = findViewById(R.id.ratingBarDetails)

        btnCancelButton = findViewById(R.id.btnCancelButton)
        btnConfirmButton = findViewById(R.id.btnConfirmButton)

        // Set event listener
        btnBack.setOnClickListener(this)
        btnCancelButton.setOnClickListener(this)
        btnConfirmButton.setOnClickListener(this)

        // Get data passed from the first activity
        val intent = getIntent()
        var item = intent.getParcelableExtra<Instrument>("InstrumentData")
        var currentBalance = intent.getFloatExtra("CurrentBalance", 0.0f)

        // Prepare for the selected accessories
        selectedAccessories = item?.rentedAccessories!!

        // Set data in order to display to UI
        txtProductNameDetail.setText(item?.name)
        txtInstrumentNameDetail.setText(item?.name)

        item?.let {
            imgProductDetail.setImageDrawable(getDrawable(it.image))
        }

        txtRentingPriceDetail.setText("$" + "%.0f".format(item?.price) + "/mo")
        txtCurrentBalanceDetail.setText("$" + "%.2f".format(currentBalance))

        // Add chips to UI
        chipDetailsArea.removeAllViews() // Delete all chips before adding new one

        // Update chip group
        item?.accessories?.forEach { accessoriesName ->
            // Create a new chip and apply styles for them
            val chip = Chip(this)
            chip.text = accessoriesName
            chip.textSize = 18f
            chip.chipCornerRadius = 40f
            chip.typeface = ResourcesCompat.getFont(this, R.font.inter_medium) // Set custom font

            // Set colors for the chip
            chip.setTextColor(ContextCompat.getColor(this, R.color.chip_text_color))
            chip.setChipBackgroundColorResource(R.color.chip_bg_color)
            chip.setChipStrokeColorResource(R.color.chip_bg_color)

            chip.isCheckable = true
            chip.isClickable = true

            // Event handler
            chip.setOnClickListener {
                if (chip.isChecked) {
                    // Chip selected
                    chip.setChipBackgroundColorResource(R.color.primary_color)
                    chip.setChipStrokeColorResource(R.color.primary_color)
                    chip.setTextColor(ContextCompat.getColor(this, R.color.white))
                    chip.chipIcon = ContextCompat.getDrawable(this, R.drawable.ic_check) // Set tick icon
                    selectedAccessories.add(accessoriesName)
                } else {
                    // Chip deselected
                    chip.setTextColor(ContextCompat.getColor(this, R.color.chip_text_color))
                    chip.setChipBackgroundColorResource(R.color.chip_bg_color)
                    chip.setChipStrokeColorResource(R.color.chip_bg_color)
                    chip.chipIcon = null
                    selectedAccessories.remove(accessoriesName)
                }
            }

            // Add chip to the chip group
            chipDetailsArea.addView(chip)
        }
    }

    // Input validation function
    private fun inputValidation() {
        var allowSubmit = true
        val emailRegex = "^[^\\s]+@[^\\s]+\\.[^\\s]+$".toRegex() // Regex reference: https://uibakery.io/regex-library/email

        // Clear all error
        txtName.setError(null)
        txtEmail.setError(null)
        txtRentingPeriod.setError(null)

        // Get input from user first
        var name = txtName.getEditText()?.getText()
        var email = txtEmail.getEditText()?.getText()
        var rentingPeriod = txtRentingPeriod.getEditText()?.getText()

        // 1st check: Name input
        if(name.toString().isEmpty()) {
            txtName.setError("Please enter your name")
            allowSubmit = false
        }

        // 2nd check: Email input
        if(email.toString().isEmpty()) {
            txtEmail.setError("Please enter your email")
            allowSubmit = false
        }
        else if(!emailRegex.matches(email.toString())) {
            txtEmail.setError("Please check email format")
            allowSubmit = false
        }

        // 3rd check: Renting period
        if(rentingPeriod.toString().isEmpty()) {
            txtRentingPeriod.setError("Please enter your renting period")
            allowSubmit = false
        }
        else if(!(rentingPeriod.toString().toInt() > 0)) {
            txtRentingPeriod.setError("Renting period must be greater than 0")
            allowSubmit = false
        }

        // 4th check: Enough balance?
        val intent = getIntent()
        val currentItemID = intent.getIntExtra("InstrumentID", 0)
        // Get current balance and price of item
        var item = intent.getParcelableExtra<Instrument>("InstrumentData")
        var currentBalance = intent.getFloatExtra("CurrentBalance", 0.0f)

        // Only check if the input are fine
        if(allowSubmit) {
            if(currentBalance - (item?.price!! * rentingPeriod.toString().toInt()) < 0) { // Using "!!" symbol to get non-null value
                showNotificationWithDismiss("You don't have enough money to rent this instrument within your renting period.")
                allowSubmit = false
            }
        }

        // Validation pass, back to intent
        if(allowSubmit) {
            // Set new attributes to the item
            item?.rented = true
            item?.rating = ratingBarDetails.rating
            item?.rentedAccessories = selectedAccessories

            // Start intent and move back to the main activity
            // Pass the new data to the first activity
            var intent = Intent()
            intent.putExtra("ConfirmedInstrumentID", currentItemID)
            intent.putExtra("NewBalance", currentBalance - (item?.price!! * rentingPeriod.toString().toInt()))
            intent.putExtra("NewInstrumentData", item)
            setResult(RESULT_OK, intent) // Set RESULT_OK flag to let the first activity know to update the data
            finish()
        }
    }

    // Function to show snackbar with dismiss button
    private fun showNotificationWithDismiss(message: String) {
        val snackbar = Snackbar.make(findViewById(R.id.main), message, 5000)
        // Set action to snackbar in order to dismiss notification
        snackbar.setAction("DISMISS") {
            snackbar.dismiss()
        }
        snackbar.show()
    }

    // Handle event when user try to back to the first screen
    override fun onBackPressed() {
        var intent = Intent()
        setResult(RESULT_CANCELED, intent) // Notify the first activity to display the cancel booking message
        finish()

        super.onBackPressed()
    }

    // Handle when user pressed
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnBack -> {
                onBackPressed() // Back to the first screen
            }
            R.id.btnCancelButton -> {
                onBackPressed() // Back to the first screen
            }
            R.id.btnConfirmButton -> {
                inputValidation() // Validate input
            }
        }
    }
}