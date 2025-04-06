package com.example.assignment3.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.assignment3.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onData
import org.hamcrest.Matchers.anything

@RunWith(AndroidJUnit4::class)
class AddEditTransactionTest {

    @Before
    fun addEditTransactionActivity_setup() {
        ActivityScenario.launch(AddEditTransactionActivity::class.java)
    }

    @After
    fun addEditTransactionActivity_tearDown() {
        release()
    }

    @Test
    fun addEditTransactionActivity_displayInputFields() {
        onView(withId(R.id.txtTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.txtAmount)).check(matches(isDisplayed()))
        onView(withId(R.id.txtDate)).check(matches(isDisplayed()))
        onView(withId(R.id.spinnerCategory)).check(matches(isDisplayed()))
    }

    @Test
    fun addEditTransactionActivity_fillFormAndSubmit() {
        onView(withId(R.id.txtTitle)).perform(typeText("Test Transaction"), closeSoftKeyboard())
        onView(withId(R.id.txtAmount)).perform(typeText("99.99"), closeSoftKeyboard())
        onView(withId(R.id.btnSubmit)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSubmit)).perform(click())
    }

    @Test
    fun addEditTransactionActivity_transactionTypeTogglesCorrectly() {
        onView(withId(R.id.btnExpense)).perform(click())
        onView(withId(R.id.spinnerCategory)).check(matches(isDisplayed()))

        onView(withId(R.id.btnIncome)).perform(click())
        onView(withId(R.id.spinnerCategory)).check(matches(isDisplayed()))
    }

    @Test
    fun addEditTransactionActivity_selectCategoryFromSpinner() {
        onView(withId(R.id.spinnerCategory)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.spinnerCategory)).check(matches(isDisplayed()))
    }
}
