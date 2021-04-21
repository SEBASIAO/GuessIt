package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore:Int) : ViewModel () {
    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished


    init {
        _score.value = finalScore
    }

    fun playAgain(){
        _eventGameFinished.value = true;
    }

    fun playAgainFinished(){
        _eventGameFinished.value = false;
    }
}