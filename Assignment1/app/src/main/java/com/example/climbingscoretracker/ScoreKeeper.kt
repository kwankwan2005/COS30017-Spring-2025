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

    // Climb method: Allow user to record a climb time
    fun climb() {
        if(!availableToClimb()) {
            Log.d("ScoreKeeper", "Failed to climb, current score: " + score.toString() + ", current hold: " + hold.toString())
            return
        }
        hold++
        addScore()
        Log.d("ScoreKeeper", "Climbed successfully, current score: " + score.toString() + ", current hold: " + hold.toString())
    }

    // Fall method
    fun fall() {
        if(!availableToFall()) {
            Log.d("ScoreKeeper", "Failed to fall, current score: " + score.toString() + ", current hold: " + hold.toString())
            return
        }
        fall = true

        if(score < 3) {
            score = 0
        }
        else {
            score -= 3
        }

        Log.d("ScoreKeeper", "Falled successfully, current score: " + score.toString() + ", current hold: " + hold.toString())
    }

    // Reset method
    fun reset() {
        score = 0
        hold = 0
        fall = false

        Log.d("ScoreKeeper", "Reset counter successfully")
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
        Log.d("ScoreKeeper", "Add score for user in hold " + hold.toString())
    }
}