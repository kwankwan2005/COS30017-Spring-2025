package com.example.assignment3.ui

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.assignment3.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionDetailTest {

    private lateinit var scenario: ActivityScenario<TransactionDetailActivity>

    @Before
    fun transactionDetailActivity_setup() {
        init()
        val intent = Intent(Intent.ACTION_MAIN).apply {
            putExtra("transaction_id", 1) // Assume that the first transaction in database
        }
        // Launch the intent
        scenario = ActivityScenario.launch(intent.setClassName(
            "com.example.assignment3",
            "com.example.assignment3.ui.TransactionDetailActivity"
        ))
    }

    @After
    fun transactionDetailActivity_tearDown() {
        release()
        scenario.close()
    }

    @Test
    fun transactionDetailActivity_displayMainViews() {
        onView(withId(R.id.txtTransaction)).check(matches(isDisplayed()))
        onView(withId(R.id.txtAmount)).check(matches(isDisplayed()))
        onView(withId(R.id.txtDate)).check(matches(isDisplayed()))
        onView(withId(R.id.txtCategory)).check(matches(isDisplayed()))
        onView(withId(R.id.btnEdit)).check(matches(isDisplayed()))
        onView(withId(R.id.btnDelete)).check(matches(isDisplayed()))
    }

    @Test
    fun transactionDetailActivity_clickEdit_shouldLaunchEditActivity() {
        onView(withId(R.id.btnEdit)).perform(click())
        intended(hasComponent(AddEditTransactionActivity::class.java.name))
    }

    @Test
    fun transactionDetailActivity_clickDelete_shouldShowDialog() {
        onView(withId(R.id.btnDelete)).perform(click())
        onView(withText("Delete transaction")).check(matches(isDisplayed()))
    }
}
