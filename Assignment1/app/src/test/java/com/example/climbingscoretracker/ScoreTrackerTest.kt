package com.example.climbingscoretracker

import org.junit.Test

import org.junit.Assert.*


class ScoreTrackerTest {

    @Test
    fun scoreTracker_ClimbToBlueZone() {
        val scoreKeeper = ScoreTracker()
        scoreKeeper.climb()
        assertEquals(scoreKeeper.getHold(), 1)
        assertEquals(scoreKeeper.getScore(), 1)
    }

    @Test
    fun scoreTracker_ClimbToGreenZone() {
        val scoreKeeper = ScoreTracker()
        for(i in 1..4) {
            scoreKeeper.climb()
        }
        assertEquals(scoreKeeper.getHold(), 4)
        assertEquals(scoreKeeper.getScore(), 5)
    }

    @Test
    fun scoreTracker_ClimbToRedZone() {
        val scoreKeeper = ScoreTracker()
        for(i in 1..7) {
            scoreKeeper.climb()
        }
        assertEquals(scoreKeeper.getHold(), 7)
        assertEquals(scoreKeeper.getScore(), 12)
    }

    @Test
    fun scoreTracker_ClimbToHighest() {
        val scoreKeeper = ScoreTracker()
        for(i in 1..9) {
            scoreKeeper.climb()
        }
        assertEquals(scoreKeeper.getHold(), 9)
        assertEquals(scoreKeeper.getScore(), 18)
    }

    @Test
    fun scoreTracker_ClimbToHighestThenClimbAgain() {
        val scoreKeeper = ScoreTracker()
        for(i in 1..9) {
            scoreKeeper.climb()
        }
        scoreKeeper.climb()
        assertEquals(scoreKeeper.getHold(), 9)
        assertEquals(scoreKeeper.getScore(), 18)
    }

    @Test
    fun scoreTracker_FallSingleTest() {
        val scoreKeeper = ScoreTracker()
        for(i in 1..5) {
            scoreKeeper.climb()
        }
        scoreKeeper.fall()
        assertEquals(scoreKeeper.getScore(), 4)
        assertEquals(scoreKeeper.getHold(), 5)
        assertFalse(scoreKeeper.availableToFall())
    }

    @Test
    fun scoreTracker_FallAfterFirstClimb_NoNegativeScores() {
        val scoreKeeper = ScoreTracker()
        scoreKeeper.climb()
        scoreKeeper.fall()
        assertEquals(scoreKeeper.getScore(), 0)
        assertEquals(scoreKeeper.getHold(), 1)
        assertFalse(scoreKeeper.availableToFall())
    }

    @Test
    fun scoreTracker_FallThenClimb() {
        val scoreKeeper = ScoreTracker()
        for(i in 1..5) {
            scoreKeeper.climb()
        }
        scoreKeeper.fall()
        scoreKeeper.climb()
        assertEquals(scoreKeeper.getScore(), 4)
        assertEquals(scoreKeeper.getHold(), 5)
        assertFalse(scoreKeeper.availableToFall())
    }

    @Test
    fun scoreTracker_FallAfterClimbToHighest() {
        val scoreKeeper = ScoreTracker()
        for(i in 1..9) {
            scoreKeeper.climb()
        }
        scoreKeeper.fall()
        assertFalse(scoreKeeper.availableToFall())
        assertEquals(scoreKeeper.getScore(), 18)
    }

    @Test
    fun scoreTracker_DoubleFall() {
        val scoreKeeper = ScoreTracker()
        for(i in 1..5) {
            scoreKeeper.climb()
        }
        scoreKeeper.fall()
        scoreKeeper.fall()
        assertEquals(scoreKeeper.getScore(), 4)
        assertEquals(scoreKeeper.getHold(), 5)
        assertFalse(scoreKeeper.availableToFall())
    }

    @Test
    fun scoreTracker_Reset() {
        val scoreKeeper = ScoreTracker()
        scoreKeeper.climb()
        scoreKeeper.reset()
        assertEquals(scoreKeeper.getScore(), 0)
        assertEquals(scoreKeeper.getHold(), 0)
    }
}