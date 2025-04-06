package com.example.assignment3.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.assignment3.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionListTest {

    private lateinit var scenario: ActivityScenario<TransactionListActivity>

    @Before
    fun transactionListActivity_setup() {
        init()
        scenario = ActivityScenario.launch(TransactionListActivity::class.java)
    }

    @After
    fun transactionListActivity_tearDown() {
        release()
        scenario.close()
    }

    @Test
    fun transactionListActivity_displayMainViews() {
        onView(withId(R.id.txtTitle)).check(matches(withText("Transactions")))
        onView(withId(R.id.rvTransactions)).check(matches(isDisplayed()))
        onView(withId(R.id.btnAdd)).check(matches(isDisplayed()))
        onView(withId(R.id.btnFilter)).check(matches(isDisplayed()))
    }

    @Test
    fun transactionListActivity_clickAdd_shouldOpenAddEditTransactionActivity() {
        onView(withId(R.id.btnAdd)).perform(click())
        intended(hasComponent(AddEditTransactionActivity::class.java.name))
    }

    @Test
    fun transactionListActivity_clickFilter_shouldDisplayDialogOrMenu() {
        onView(withId(R.id.btnFilter)).perform(click())
        onView(withText("Income")).check(matches(isDisplayed()))
    }
}
