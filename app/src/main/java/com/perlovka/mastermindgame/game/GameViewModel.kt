package com.perlovka.mastermindgame.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.*
import com.perlovka.mastermindgame.SecretNumberApi
import com.perlovka.mastermindgame.convertResultToMessage
import com.perlovka.mastermindgame.generateSecret
import com.perlovka.mastermindgame.model.Guess
import com.perlovka.mastermindgame.resultMessage
import kotlinx.coroutines.launch

// These are the three different types of API request status
enum class SecretNumberApiStatus { LOADING, ERROR, DONE }

// These are the three different types of buzzing in the game. Buzz pattern is the number of
// milliseconds each interval of buzzing and non-buzzing takes.
enum class BuzzType(val pattern: LongArray) {
    GAME_OVER(longArrayOf(0, 200)),
    COUNTDOWN_PANIC(longArrayOf(0, 2000)),
    NO_BUZZ(longArrayOf(0))
}

/**
 * The [ViewModel] that is attached to the [GameFragment].
 */
class GameViewModel : ViewModel() {
    // The current attempt
    private val _attempts = MutableLiveData<Int>()
    val attempts: LiveData<Int>
        get() = _attempts

    // The current guess object
    private val _currentGuess = MutableLiveData<Guess>()
    val currentGuess: LiveData<Guess>
        get() = _currentGuess

    // The current guess number
    private val _currentGuessNumber = MutableLiveData<String>()

    val currentGuessNumber: LiveData<String>
        get() = _currentGuessNumber

    companion object {
        // This is when the game is over
        const val DONE = 0L

        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L

        // This is the total time of the game
        const val COUNTDOWN_TIME = 300000L
    }

    private val timer: CountDownTimer
    private val _timeLeft = MutableLiveData<Long>()
    val timeLeft: LiveData<Long>
        get() = _timeLeft

    val timeLeftString = Transformations.map(timeLeft, DateUtils::formatElapsedTime)

    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    // The LiveData that stores the status of the most recent request
    private val _status = MutableLiveData<SecretNumberApiStatus>()
    val status: LiveData<SecretNumberApiStatus>
        get() = _status

    // Variable to store random number retrieved from network request
    var secretNumber: String = generateSecret()

    var secretNumberMap: MutableMap<Char, Int> = HashMap<Char, Int>()
    // Variable to store result of GuessCheck function
    private val _result = MutableLiveData<String>()
    val result: LiveData<String>
        get() = _result

    // The LiveData that stores the status of the game
    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished: LiveData<Boolean>
        get() = _eventGameFinished

    // List of guesses
    private var guessItemList = mutableListOf<Guess>()
    private val _guessList = MutableLiveData<List<Guess>>()
    val guessList: LiveData<List<Guess>>
        get() = _guessList

    // The LiveData that stores the visibility parameter of Submit Button
    private val _submitButtonClickable = MutableLiveData<Boolean>()
    val submitButtonClickable: LiveData<Boolean>
        get() = _submitButtonClickable

    init {
        _attempts.value = 10
        _currentGuess.value = Guess()
        _currentGuessNumber.value = Guess().number
        _eventGameFinished.value = false
        _submitButtonClickable.value = false

        //Call to getSecretNumber() network request to inflate secretNumber
        getSecretNumber()

        // Initialize timer object
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                timerTick(millisUntilFinished)
            }

            override fun onFinish() {
                timerFinished()
            }
        }

        timer.start()
        Log.i("GameViewModel", "GameViewModel created")
    }

    // Method to add a new guess to the guesses list
    private fun addGuessToAnswerList(guess: Guess?) {
        guess?.let {
            guessItemList.add(it)
            _guessList.value = guessItemList
        }
    }

    //Method to connect to Internet and inflate secret random number
    private fun getSecretNumber() {
        viewModelScope.launch {
            _status.value = SecretNumberApiStatus.LOADING
            try {

                secretNumber =
                    SecretNumberApi.retrofitService.getNumber(4, 0, 7, 1, 10, "plain", "new")
                        .filter { it.isDigit() }
                for(i in secretNumber.indices){
                    val count =  secretNumberMap.getOrPut(secretNumber[i]){0}
                    secretNumberMap[secretNumber[i]] = count + 1
                }
                _status.value = SecretNumberApiStatus.DONE
                Log.i("GameViewModel", "Request done  secret number:  $secretNumber")

            } catch (e: Exception) {
                secretNumber = generateSecret()
                _status.value = SecretNumberApiStatus.ERROR
                Log.i("GameViewModel", "Request error:  $secretNumber")
                Log.i("GameViewModel", e.message ?: "Exception occur")
            }
        }
    }

    /**
     *   Method to change game status
     */
    fun onGameFinishedComplete() {
        _eventGameFinished.value = false
    }

    /**
     * Executes when the SUBMIT button is clicked.
     * Check if guess matches secret number
     */
    fun checkGuess() {

        //guessMatch = how many digits are in the right position
        var guessMatchCounter = 0
        // appearence = how many digits are in the secretnumber
        var appearenceCounter = 0

        val guess = _currentGuess.value

        // Check appearance and matches if guess is not null
        guess?.let {
            for (i in it.number.indices) {
                val n = it.number[i]
                if (secretNumberMap.containsKey(n)) {
                    if (secretNumber[i] == n) {
                        guessMatchCounter = guessMatchCounter.plus(1)
                        secretNumberMap[n] = secretNumberMap[n]!! - 1
                    } else {
                        if (secretNumberMap[n]!! > 0) {
                            appearenceCounter = appearenceCounter.plus(1)
                            secretNumberMap[n] = secretNumberMap[n]!! - 1
                        }
                    }
                }
            }
            for(i in 0 until guessMatchCounter){
                it.guessRightNumberRightPositionCounter[i] = 2
            }
            it.rightNumberWrongPositionCounter = appearenceCounter
            it.message = convertResultToMessage(guessMatchCounter, appearenceCounter)
            _currentGuessNumber.value = it.number
            _submitButtonClickable.value = false

            // Added Current guess to history of guesses list
            addGuessToAnswerList(it)
        }
        // Decrease number of Attempts left
        _attempts.value = (_attempts.value)?.minus(1)

        // Check if number of attempts ends or secret number is guessed
        if (_attempts.value == 0 || guessMatchCounter == 4) {
            _result.value = resultMessage(guessMatchCounter)
            _eventGameFinished.value = true
        }
        // Create new guess object
        val newGuess = Guess()
        _currentGuessNumber.value = newGuess.number
        _currentGuess.value = newGuess
    }

    /**
     * Executes when 0 - 7 Number button is clicked.
     */
    fun numberSelected(number: Int) {
        val guess = _currentGuess.value
        guess?.let {
            val builder = StringBuilder(it.number)
            when (it.size) {
                in 0..2 -> it.number = builder.append(number).toString()
                3 -> {
                    it.number = builder.append(number).toString()
                    _submitButtonClickable.value = true
                }
            }
            _currentGuessNumber.value = it.number
            it.size = it.size.plus(1)
        }
    }

    /**
     * Executes when the CLEAR button is clicked.
     */
    fun reset() {
        val guess = _currentGuess.value
        val newGuess = Guess()
        guess?.let {
            it.number = newGuess.number
            it.size = newGuess.size
            _submitButtonClickable.value = false
            _currentGuessNumber.value = it.number
            _currentGuess.value = Guess()
        }
    }

    /**
     * Method to change eventBuzz status.
     */
    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

    //Executes when the time on timer is end.
    private fun timerFinished() {
        _attempts.value = 0
        _timeLeft.value = DONE
        _eventBuzz.value = BuzzType.GAME_OVER
        _result.value = resultMessage(0)
        _eventGameFinished.value = true
    }

    private fun timerTick(millisUntilFinished: Long) {
        _timeLeft.value = millisUntilFinished / ONE_SECOND
        if (millisUntilFinished / ONE_SECOND <= 10L) {
            _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
        }
    }

    /**
     * Called when the ViewModel is dismantled.
     **/
    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed")
    }


}