package com.example.climbingscoretracker

import android.util.Log

class ScoreTracker {
    private var score: Int = 0
    private var hold: Int = 0
    private var fall: Boolean = false

    // Get methods, return information that the view needs
    fun getScore(): Int {
        return score
    }

    fun getHold(): Int {
        return hold
    }

    fun getFallStatus(): Boolean {
        return fall
    }

    // Climb method: Allow user to record a climb time
    fun climb() {
        if(!availableToClimb()) {
            return
        }
        hold++
        addScore()
    }

    // Fall method
    fun fall() {
        if(!availableToFall()) {
            return
        }
        fall = true

        if(score < 3) {
            score = 0
        }
        else {
            score -= 3
        }
    }

    // Reset method
    fun reset() {
        score = 0
        hold = 0
        fall = false
    }

    // Return if it is available to fall
    fun availableToFall(): Boolean {
        if (hold == 0 || hold == 9) return false
        if (fall) return false
        return true
    }

    // Return if it is available to climb
    fun availableToClimb(): Boolean {
        if (fall) return false
        if (hold == 9) return false
        return true
    }

    // Add score based on current zone of user
    private fun addScore() {
        when (hold) {
            in 1..3 -> {
                score += 1
            }
            in 4..6 -> {
                score += 2
            }
            else -> {
                score += 3
            }
        }
    }
}