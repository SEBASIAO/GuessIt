package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel


private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel:ViewModel (){

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 30000L

        private const val COUNTDOWN_PANIC_SECONDS = 5000L
    }

    // The current word
     private val _word = MutableLiveData<String>()
    val word: LiveData<String>
    get() = _word

    // The current score
     private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
    get() = _score

    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished : LiveData<Boolean>
    get() = _eventGameFinished

    private val timer: CountDownTimer

    private val _currentTimer = MutableLiveData<Long>()
    val currentTimer:LiveData<Long>
    get() = _currentTimer

    val currentTimeString = Transformations.map(currentTimer, {time ->
        DateUtils.formatElapsedTime(time)
    })
    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    init {
        Log.i("GameViewModel","GameViewModel was created!")
        _score.value = 0
        resetList()
        nextWord()
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                // TODO implement what should happen each tick of the timer
                _currentTimer.value = (millisUntilFinished/ ONE_SECOND)
                if ((millisUntilFinished/ ONE_SECOND) <= COUNTDOWN_PANIC_SECONDS){
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                // TODO implement what should happen when the timer finishes
                _currentTimer.value = DONE
                _eventBuzz.value = BuzzType.GAME_OVER
                _eventGameFinished.value = true
            }
        }
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel","GameViewModel was destroyed!")
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            //gameFinished()
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

     fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

     fun onCorrect() {
         _score.value = (score.value)?.plus(1)
         _eventBuzz.value = BuzzType.CORRECT
        nextWord()
    }

    fun onGameFinishedComplete(){
        _eventGameFinished.value = false
    }

    fun onBuzzComplete(){
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

}