package com.example.climbingscoretracker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel : ViewModel() {
    private val _scoreKeeper = MutableLiveData<ScoreTracker>()
    var scoreKeeper = ScoreTracker()

    val scoreTracker: LiveData<ScoreTracker>
        get() = _scoreKeeper

    fun climb() {
        scoreKeeper.climb()
        _scoreKeeper.postValue(scoreKeeper)
        Log.d("ViewModel", "Climb behaviour: New value updated")
    }

    fun fall() {
        scoreKeeper.fall()
        _scoreKeeper.postValue(scoreKeeper)
        Log.d("ViewModel", "Fall behaviour: New value updated")
    }

    fun reset() {
        scoreKeeper.reset()
        _scoreKeeper.postValue(scoreKeeper)
        Log.d("ViewModel", "Reset behaviour: New value updated")
    }

    override fun onCleared() {
        super.onCleared()
    }
}