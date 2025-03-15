package com.example.assignment2

import android.R.attr.duration
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity(), View.OnClickListener {
    //// SETTING UP THE ELEMENTS ON THE SCREEN ////
    lateinit var txtStatus: TextView
    lateinit var txtSubStatus: TextView
    lateinit var txtBalance: TextView

    lateinit var imgLeftSide: ImageView
    lateinit var imgMainSide: ImageView
    lateinit var imgRightSide: ImageView

    lateinit var txtStatusItem: TextView
    lateinit var chipAttributes: ChipGroup

    lateinit var accessoriesSeparator: LinearLayout
    lateinit var chipAccessories: ChipGroup

    lateinit var txtInstrumentNameDetail: TextView
    lateinit var ratingBar: RatingBar
    lateinit var txtRentingPriceDetail: TextView

    lateinit var btnBorrow: Button
    lateinit var btnNext: Button

    //// SETTING UP THE VARIABLES FOR THE APP ////
    var currentInstrument = 0
    var currentBalance = 550.00f;
    var authenticatedEmail = "";
    var authenticatedName = "";

    //// DUMMY DATA FOR THE APP ////
    var acousticGuitar = Instrument(
        "Acoustic Guitar",
        R.drawable.acoustic_guitar,
        180.0f,
        2.5f,
        false,
        mutableListOf("Hot", "Acoustic", "Guitar"),
        mutableListOf("Capo", "Strap", "Tuner"),
        mutableListOf()
    )

    var electricGuitar = Instrument(
        "Electric Guitar",
        R.drawable.electric_guitar,
        250.0f,
        4f,
        false,
        mutableListOf("Electric", "Teens", "Guitar"),
        mutableListOf("Guitar case", "Stickers"),
        mutableListOf()
    )

    var kalimba = Instrument(
        "Kalimba",
        R.drawable.kalimba,
        365.0f,
        3f,
        false,
        mutableListOf("Traditional"),
        mutableListOf("Protective case", "Microphone"),
        mutableListOf()
    )

    var piano = Instrument(
        "Piano",
        R.drawable.piano,
        176.0f,
        1.6f,
        false,
        mutableListOf("Classic", "Piano", "Rare"),
        mutableListOf("Dust cover", "Headphones", "Piano bench"),
        mutableListOf()
    )

    var instrumentList = mutableListOf<Instrument>(acousticGuitar, electricGuitar, kalimba, piano)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set element id to variables
        txtStatus = findViewById(R.id.txtStatus)
        txtSubStatus = findViewById(R.id.txtSubStatus)
        txtBalance = findViewById(R.id.txtCurrentBalance)

        imgLeftSide = findViewById(R.id.imgLeftSide)
        imgMainSide = findViewById(R.id.imgMainSide)
        imgRightSide = findViewById(R.id.imgRightSide)

        txtStatusItem = findViewById(R.id.txtStatusItem)
        chipAttributes = findViewById(R.id.chipAttributes)

        accessoriesSeparator = findViewById(R.id.accessoriesSeparator)
        chipAccessories = findViewById(R.id.chipAccessories)

        txtInstrumentNameDetail = findViewById(R.id.txtInstrumentNameDetail)
        ratingBar = findViewById(R.id.ratingBar)
        txtRentingPriceDetail = findViewById(R.id.txtRentingPriceDetail)

        btnBorrow = findViewById(R.id.btnBorrowButton)
        btnNext = findViewById(R.id.btnNextButton)

        // Setup events listener
        btnNext.setOnClickListener(this)
        btnBorrow.setOnClickListener(this)
        txtBalance.setOnClickListener(this)

        // Update greeting status based on current timestamp
        val currentHourDate = SimpleDateFormat("H")
        val currentTime = currentHourDate.format(Date()).toInt()
        if(currentTime >= 12 && currentTime < 17) {
            txtStatus.setText("Good afternoon \uD83C\uDF25\uFE0F")
        }
        else if (currentTime >= 17 && currentTime <= 23 || currentTime >= 0 && currentTime <= 6) {
            txtStatus.setText("Good evening \uD83C\uDF19")
        }
        else {
            txtStatus.setText("Good morning ☀\uFE0F")
        }

        // Update UI
        updateSideBar()
        updateSubStatus()
        updateInstrumentData()

        Log.d("MainActivity", "Initialized the screen")
    }

    // Update UI: Update substatus
    fun updateSubStatus() {
        Log.d("MainActivity", "Current authenticate data: " + authenticatedEmail)
        if(authenticatedEmail.isEmpty()) {
            txtSubStatus.setText("Ready to rent something?")
        }
        else {
            txtSubStatus.setText("What's up, " + authenticatedName + "? \uD83D\uDC4B")
        }
    }

    // Update UI: Authentication and balance
    fun updateSideBar() {
        if(authenticatedEmail.isEmpty()) {
            txtBalance.setText("Sign in")
        }
        else {
            txtBalance.setText("$" + "%.2f".format(currentBalance))
        }
        Log.d("MainActivity", "Sidebar updated")
    }

    // Update UI: Instrument data
    fun updateInstrumentData() {
        // Get current item first
        var item = instrumentList[currentInstrument]

        // Display picture in the carousel (main side)
        imgMainSide.setImageResource(item.image)

        // Display picture in the carousel (left side)
        if(currentInstrument == 0) {
            var lastItem = instrumentList[instrumentList.size - 1]
            Log.d("Item", lastItem.image.toString())
            imgLeftSide.setImageResource(lastItem.image)
        }
        else {
            var previousItem = instrumentList[currentInstrument - 1]
            imgLeftSide.setImageResource(previousItem.image)
        }

        // Display picture in the carousel (right side)
        if(currentInstrument == instrumentList.size - 1) {
            var firstItem = instrumentList[0]
            imgRightSide.setImageResource(firstItem.image)
        }
        else {
            var nextItem = instrumentList[currentInstrument + 1]
            imgRightSide.setImageResource(nextItem.image)
        }

        // Display rented status
        // And also disabled renting button
        if(item.rented) {
            txtStatusItem.visibility = View.VISIBLE
            btnBorrow.isClickable = false
            btnBorrow.setBackgroundColor(resources.getColor(R.color.default_stroke))
            btnBorrow.setTextColor(resources.getColor(R.color.secondary_stroke))
        }
        else {
            txtStatusItem.visibility = View.GONE
            btnBorrow.isClickable = true
            btnBorrow.setBackgroundColor(resources.getColor(R.color.primary_color))
            btnBorrow.setTextColor(resources.getColor(R.color.white))
        }

        chipAttributes.removeAllViews() // Delete all chips before adding new one

        // Update chip group
        item.tags.forEach { tagName ->
            // Create a new chip and apply styles for them
            val chip = Chip(this)
            chip.text = tagName
            chip.textSize = 18f
            chip.chipCornerRadius = 40f
            chip.typeface = ResourcesCompat.getFont(this, R.font.inter_medium) // Set custom font

            // Set colors for the chip
            chip.setTextColor(ContextCompat.getColor(this, R.color.chip_text_color))
            chip.setChipBackgroundColorResource(R.color.chip_bg_color)
            chip.setChipStrokeColorResource(R.color.chip_bg_color)

            // Add chip to the chip group
            chipAttributes.addView(chip)
        }

        // Update product name
        txtInstrumentNameDetail.setText(item.name)

        // Update rating bar
        ratingBar.rating = item.rating

        // Update renting price
        txtRentingPriceDetail.setText("$" + "%.0f".format(item.price) + "/mo")

        chipAccessories.removeAllViews() // Delete all accessories chips before adding new one

        if (item.rentedAccessories.size == 0) {
            accessoriesSeparator.visibility = View.GONE
            chipAttributes.visibility = View.VISIBLE
        }
        else {
            accessoriesSeparator.visibility = View.VISIBLE
            chipAttributes.visibility = View.GONE

            // Create chips for rented accessories
            item.rentedAccessories.forEach { accessory ->
                // Create a new chip and apply styles for them
                val chip = Chip(this)
                chip.text = accessory
                chip.textSize = 18f
                chip.chipCornerRadius = 40f
                chip.typeface = ResourcesCompat.getFont(this, R.font.inter_medium) // Set custom font

                // Set colors for the chip
                chip.setTextColor(ContextCompat.getColor(this, R.color.chip_text_color))
                chip.setChipBackgroundColorResource(R.color.chip_bg_color)
                chip.setChipStrokeColorResource(R.color.chip_bg_color)

                // Add chip to the chip group
                chipAccessories.addView(chip)
            }
        }

        Log.d("MainActivity", "Instrument data updated")
    }

    // Event handler
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.txtCurrentBalance -> {
                Log.d("MainActivity", "Current balance clicked")
                if(authenticatedEmail.isEmpty()) {
                    val intent = Intent(this, LoginActivity::class.java)
                    getLoginResult.launch(intent)
                }
            }
            R.id.btnBorrowButton -> { // Borrow button
                Log.d("MainActivity", "Borrow button clicked")
                // Check if user authenticated?
                if(authenticatedEmail.isEmpty()) {
                    showNotificationWithDismiss("Please sign in before continue to borrow.")
                }
                // Check if user has enough balance to rent at least a month
                else if(currentBalance < instrumentList[currentInstrument].price) {
                    showNotificationWithDismiss("You don't have enough money to rent this instrument for at least a month.")
                }
                else {
                    // Save data and pass to the booking screen
                    val intent = Intent(this, DetailRenting::class.java)
                    intent.putExtra("InstrumentID", currentInstrument)
                    intent.putExtra("CurrentBalance", currentBalance)
                    intent.putExtra("InstrumentData", instrumentList[currentInstrument])
                    intent.putExtra("DisplayName", authenticatedName)
                    intent.putExtra("DisplayEmail", authenticatedEmail)
                    getResult.launch(intent)
                    Log.d("MainActivity", "Switched intent successfully: Detail renting form")
                }
            }
            R.id.btnNextButton -> {
                Log.d("MainActivity", "Next button clicked")
                // Move to the next instrument in the list
                currentInstrument = (currentInstrument + 1) % (instrumentList.size)
                updateInstrumentData()
            }
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

    // Register for handling booking data
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Log.d("MainActivity", "Receive result from renting form")
        if (it.resultCode == Activity.RESULT_OK) {
            // Update new balance
            currentBalance = it.data?.getFloatExtra("NewBalance", 0.0f)!!

            // Update instrument id
            currentInstrument = it.data?.getIntExtra("ConfirmedInstrumentID", 0)!!

            // Update new instrument data
            instrumentList[currentInstrument] = it.data?.getParcelableExtra<Instrument>("NewInstrumentData")!!

            // Display success notification
            showNotificationWithDismiss("Booking successfully made for " + instrumentList[currentInstrument].name + "!")

            // Re-update UI
            updateInstrumentData()
            updateSideBar()
        }
        else {
            showNotificationWithDismiss("The booking has been cancelled.")
        }
    }

    // Register for handling authentication
    private val getLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Log.d("MainActivity", "Receive result from authenticating screen")
        if (it.resultCode == Activity.RESULT_OK) {
            // Update authentication data
            authenticatedName = it.data?.getStringExtra("DisplayName")!!
            authenticatedEmail = it.data?.getStringExtra("UserEmail")!!
            // Display success notification
            showNotificationWithDismiss("Signed in successfully as " + authenticatedName + ".")
            // Re-update UI
            updateSideBar()
            updateSubStatus()
        }
    }

}