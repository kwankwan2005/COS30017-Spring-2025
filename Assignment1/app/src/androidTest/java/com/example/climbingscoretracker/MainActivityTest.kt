package com.example.climbingscoretracker

import android.content.pm.ActivityInfo
import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.device.DeviceInteraction.Companion.setScreenOrientation
import androidx.test.espresso.device.EspressoDevice.Companion.onDevice
import androidx.test.espresso.device.action.ScreenOrientation
import androidx.test.espresso.device.rules.ScreenOrientationRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasTextColor
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isNotEnabled
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
    @get:Rule (order = 1)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule (order = 2)
    val screenOrientationRule: ScreenOrientationRule = ScreenOrientationRule(ScreenOrientation.PORTRAIT)

    @Test
    fun mainActivity_initializeAppTest() {
        onView(withId(R.id.btnClimb)).check(matches(isEnabled()))
        onView(withId(R.id.btnFall)).check(matches(isNotEnabled()))
        onView(withId(R.id.txtScoreNumber)).check(matches(withText("0")))
    }

    @Test
    fun mainActivity_ClimbToBlueZoneTest() {
        onView(withId(R.id.btnClimb)).perform(click())
        onView(withId(R.id.btnFall)).check(matches(isEnabled()))
        onView(withId(R.id.txtScoreNumber)).check(matches(withText("1")))
    }

    @Test
    fun mainActivity_ClimbToGreenZoneTest() {
        for(i in 1..4) {
            onView(withId(R.id.btnClimb)).perform(click())
        }
        onView(withId(R.id.btnFall)).check(matches(isEnabled()))
        onView(withId(R.id.txtScoreNumber)).check(matches(withText("5")))
    }

    @Test
    fun mainActivity_ClimbToRedZoneTest() {
        for(i in 1..7) {
            onView(withId(R.id.btnClimb)).perform(click())
        }
        onView(withId(R.id.btnFall)).check(matches(isEnabled()))
        onView(withId(R.id.txtScoreNumber)).check(matches(withText("12")))
    }

    @Test
    fun mainActivity_ClimbToHighestTest() {
        for(i in 1..9) {
            onView(withId(R.id.btnClimb)).perform(click())
        }
        onView(withId(R.id.btnClimb)).check(matches(isNotEnabled()))
        onView(withId(R.id.btnFall)).check(matches(isNotEnabled()))
        onView(withId(R.id.txtScoreNumber)).check(matches(withText("18")))
    }

    @Test
    fun mainActivity_FallTest() {
        for(i in 1..5) {
            onView(withId(R.id.btnClimb)).perform(click())
        }
        onView(withId(R.id.btnFall)).perform(click())
        onView(withId(R.id.txtScoreNumber)).check(matches(withText("4")))
        onView(withId(R.id.btnClimb)).check(matches(isNotEnabled()))
        onView(withId(R.id.btnFall)).check(matches(isNotEnabled()))
    }

    @Test
    fun mainActivity_FallAfterFirstClimbTest() {
        onView(withId(R.id.btnClimb)).perform(click())
        onView(withId(R.id.btnFall)).perform(click())
        onView(withId(R.id.txtScoreNumber)).check(matches(withText("0")))
        onView(withId(R.id.btnClimb)).check(matches(isNotEnabled()))
        onView(withId(R.id.btnFall)).check(matches(isNotEnabled()))
    }

    @Test
    fun mainActivity_ResetTest() {
        for(i in 1..5) {
            onView(withId(R.id.btnClimb)).perform(click())
        }
        onView(withId(R.id.btnReset)).perform(click())
        onView(withId(R.id.txtScoreNumber)).check(matches(withText("0")))
        onView(withId(R.id.btnClimb)).check(matches(isEnabled()))
        onView(withId(R.id.btnFall)).check(matches(isNotEnabled()))
    }

    @Test
    fun mainActivity_HorizontalRotationTest() {
        for(i in 1..5) {
            onView(withId(R.id.btnClimb)).perform(click())
        }

        onView(withId(R.id.txtScoreNumber)).check(matches(withText("7")))
        onView(withId(R.id.btnClimb)).check(matches(isEnabled()))
        onView(withId(R.id.btnFall)).check(matches(isEnabled()))

        onDevice().setScreenOrientation(ScreenOrientation.LANDSCAPE)

        onView(withId(R.id.txtScoreNumber)).check(matches(withText("7")))
        onView(withId(R.id.btnClimb)).check(matches(isEnabled()))
        onView(withId(R.id.btnFall)).check(matches(isEnabled()))

        onView(withId(R.id.btnClimb)).perform(click())

        onDevice().setScreenOrientation(ScreenOrientation.PORTRAIT)

        onView(withId(R.id.txtScoreNumber)).check(matches(withText("9")))
        onView(withId(R.id.btnClimb)).check(matches(isEnabled()))
        onView(withId(R.id.btnFall)).check(matches(isEnabled()))
    }
}