package com.example.assignment2

import android.os.SystemClock
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isNotEnabled
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    fun checkLoginState() {
        onView(withId(R.id.txtCurrentBalance)).perform(click())
        onView(withId(R.id.btnTestingLogin)).perform(click())
        onView(withId(R.id.txtSubStatus)).check(matches(withText("What's up, Test? \uD83D\uDC4B")))
    }

    @Test
    fun mainActivity_BeforeSignIn_InitializeAppTest() {
        onView(withId(R.id.txtSubStatus)).check(matches(withText("Ready to rent something?")))
        onView(withId(R.id.btnNextButton)).check(matches(isEnabled()))
        onView(withId(R.id.btnBorrowButton)).check(matches(isEnabled()))
        onView(withId(R.id.txtInstrumentNameDetail)).check(matches(withText("Acoustic Guitar")))
        onView(withId(R.id.txtCurrentBalance)).check(matches(withText("Sign in")))
    }

    @Test
    fun mainActivity_BeforeSignIn_ClickBorrow() {
        onView(withId(R.id.btnBorrowButton)).check(matches(isEnabled()))
        onView(withId(R.id.btnBorrowButton)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Please sign in before continue to borrow.")))
    }

    @Test
    fun mainActivity_BeforeSignIn_ClickOnNextFirstTime() {
        onView(withId(R.id.btnNextButton)).check(matches(isEnabled()))
        onView(withId(R.id.btnNextButton)).perform(click())
        onView(withId(R.id.txtInstrumentNameDetail)).check(matches(withText("Electric Guitar")))
        onView(withId(R.id.txtRentingPriceDetail)).check(matches(withText("$250/mo")))
    }

    @Test
    fun mainActivity_BeforeSignIn_ClickOnNextToTheLastItem() {
        onView(withId(R.id.btnNextButton)).check(matches(isEnabled()))
        for(i in 1..3) {
            onView(withId(R.id.btnNextButton)).perform(click())
        }
        onView(withId(R.id.txtInstrumentNameDetail)).check(matches(withText("Piano")))
        onView(withId(R.id.txtRentingPriceDetail)).check(matches(withText("$176/mo")))
    }

    @Test
    fun mainActivity_BeforeSignIn_ClickOnNextToTheFirstItem() {
        onView(withId(R.id.btnNextButton)).check(matches(isEnabled()))
        for(i in 1..4) {
            onView(withId(R.id.btnNextButton)).perform(click())
        }
        onView(withId(R.id.txtInstrumentNameDetail)).check(matches(withText("Acoustic Guitar")))
        onView(withId(R.id.txtRentingPriceDetail)).check(matches(withText("$180/mo")))
    }

    @Test
    fun mainActivity_SigningIn_ClickBalanceToSignIn() {
        onView(withId(R.id.txtCurrentBalance)).perform(click())
        onView(withId(R.id.btnSignIn)).check(matches(isEnabled()))
        onView(withId(R.id.btnSignIn)).perform(click())
    }

    @Test
    fun mainActivity_SigningIn_SignInTestingPurpose() {
        checkLoginState()
        onView(withId(R.id.txtCurrentBalance)).check(matches(withText("$550.00")))
    }

    @Test
    fun mainActivity_BorrowInstrument_CheckInformation() {
        checkLoginState()
        onView(withId(com.google.android.material.R.id.snackbar_action))
            .perform(click())
        onView(withId(R.id.btnBorrowButton)).check(matches(isEnabled()))
        onView(withId(R.id.btnBorrowButton)).perform(click())
        onView(withId(R.id.txtProductNameDetail)).check(matches(withText("Acoustic Guitar")))
        onView(withId(R.id.txtInstrumentNameDetail)).check(matches(withText("Acoustic Guitar")))
        onView(withId(R.id.txtRentingPriceDetail)).check(matches(withText("$180")))

        onView(withId(R.id.txtNameEditText)).check(matches(withText("Test")))
        onView(withId(R.id.txtEmailEditText)).check(matches(withText("test@test.com")))
    }

    @Test
    fun mainActivity_BorrowInstrument_LeaveRentingPeriodInvalid() {
        checkLoginState()
        onView(withId(com.google.android.material.R.id.snackbar_action))
            .perform(click())
        onView(withId(R.id.btnBorrowButton)).check(matches(isEnabled()))
        onView(withId(R.id.btnBorrowButton)).perform(click())

        onView(withId(R.id.txtRentingPeriodEditText)).perform(clearText())
        onView(withId(R.id.txtRentingPriceDetail)).check(matches(withText("$180")))
    }

    @Test
    fun mainActivity_BorrowInstrument_LeaveRentingPeriodTooHigh() {
        checkLoginState()
        onView(withId(com.google.android.material.R.id.snackbar_action))
            .perform(click())
        onView(withId(R.id.btnBorrowButton)).check(matches(isEnabled()))
        onView(withId(R.id.btnBorrowButton)).perform(click())

        onView(withId(R.id.txtRentingPeriodEditText)).perform(clearText())
        onView(withId(R.id.txtRentingPeriodEditText)).perform(typeText("12"), closeSoftKeyboard())

        onView(withId(R.id.btnConfirmButton)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("You don't have enough money to rent this instrument within your renting period.")))
    }

    @Test
    fun mainActivity_BorrowInstrument_ValidRentingPeriod() {
        checkLoginState()
        onView(withId(com.google.android.material.R.id.snackbar_action))
            .perform(click())

        onView(withId(R.id.btnBorrowButton)).check(matches(isEnabled()))
        onView(withId(R.id.btnBorrowButton)).perform(click())

        onView(withId(R.id.btnConfirmButton)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Booking successfully made for Acoustic Guitar!")))
        onView(withId(R.id.txtCurrentBalance)).check(matches(withText("$370.00")))
        onView(withId(R.id.txtStatusItem)).check(matches(withText("RENTED")))
    }
}