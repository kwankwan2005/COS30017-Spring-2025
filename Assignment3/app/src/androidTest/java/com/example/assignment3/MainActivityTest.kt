package com.example.assignment3.ui

import android.content.Intent
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
class MainActivityTest {

    @Before
    fun mainActivity_setup() {
        init()
        ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun mainActivity_tearDown() {
        release()
    }

    @Test
    fun mainActivity_displayBalanceTextView() {
        onView(withId(R.id.txtBalance)).check(matches(isDisplayed()))
    }

    @Test
    fun mainActivity_addButton_opensAddEditTransactionActivity() {
        onView(withId(R.id.btnAdd)).perform(click())
        intended(hasComponent(AddEditTransactionActivity::class.java.name))
    }

    @Test
    fun mainActivity_viewAllButton_opensTransactionListActivity() {
        onView(withId(R.id.btnViewAll)).perform(click())
        intended(hasComponent(TransactionListActivity::class.java.name))
    }
}
