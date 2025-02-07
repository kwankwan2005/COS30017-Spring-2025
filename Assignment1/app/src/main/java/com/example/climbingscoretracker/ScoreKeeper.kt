package com.example.climbingscoretracker

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
        if(!availableToClimb()) return
        hold++
        addScore()
    }

    fun fall() {
        if(!availableToFall()) return
        fall = true

        if(score < 3) {
            score = 0
        }
        else {
            score -= 3
        }
    }

    fun reset() {
        score = 0
        hold = 0
        fall = false
    }

    fun availableToFall(): Boolean {
        if (hold == 0 || hold == 9) return false
        if (fall) return false
        return true
    }

    fun availableToClimb(): Boolean {
        if (fall) return false
        if (hold == 9) return false
        return true
    }

    // Add score based on current hold of user
    private fun addScore() {
        if(hold in 1..3) {
            score += 1
        }
        else if(hold in 4..6) {
            score += 2
        }
        else {
            score += 3
        }
    }
}