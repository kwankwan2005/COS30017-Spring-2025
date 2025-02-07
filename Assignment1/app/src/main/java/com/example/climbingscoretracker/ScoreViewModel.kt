package com.example.climbingscoretracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel : ViewModel() {
    private val _scoreKeeper = MutableLiveData<ScoreTracker>()
    var scoreKeeper = ScoreTracker();

    val scoreTracker: LiveData<ScoreTracker>
        get() = _scoreKeeper

    fun climb() {
        scoreKeeper.climb();
        _scoreKeeper.postValue(scoreKeeper);
    }

    fun fall() {
        scoreKeeper.fall();
        _scoreKeeper.postValue(scoreKeeper);
    }

    fun reset() {
        scoreKeeper.reset();
        _scoreKeeper.postValue(scoreKeeper);
    }

    override fun onCleared() {
        super.onCleared()
    }
}