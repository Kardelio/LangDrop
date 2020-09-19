package bk.personal.com.langdrop.game.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bk.personal.com.langdrop.game.repository.IWordRepository
import bk.personal.com.langdrop.game.view.GameFragment
import bk.personal.com.langdrop.model.GameWordPair

sealed class GameState {
    data class Active(val activePair: GameWordPair) : GameState()
    object Pre : GameState()
    object Over : GameState()
}

class GameViewModel @ViewModelInject constructor(private val repo: IWordRepository) : ViewModel() {

    private val _gameState: MutableLiveData<GameState> = MutableLiveData()
    val gameState: LiveData<GameState> = _gameState
    private var currentWordPair: GameWordPair? = null

    private val _score: MutableLiveData<Int> = MutableLiveData()
    val score: LiveData<Int> = _score
    private var currentScore = 0

    private val _lives: MutableLiveData<Int> = MutableLiveData()
    val lives: LiveData<Int> = _lives
    private var currentLives = GameFragment.PLAYER_MAX_LIVES

    init {
        reloadGame()
    }

    fun startGame() {
        currentLives = GameFragment.PLAYER_MAX_LIVES
        currentScore = 0
        _lives.postValue(currentLives)
        _score.postValue(currentScore)
        nextWordPair()
    }

    fun submittedAnswer(isCorrect: Boolean?) {
        if (isCorrect == null) {
            loseALife()
        } else {
            currentWordPair?.let {
                if (isCorrect == it.correct) {
                    gainAPoint()
                } else {
                    loseALife()
                }
            }
        }

        if (currentLives > 0) {
            nextWordPair()
        } else {
            _gameState.postValue(GameState.Over)
        }
    }

    fun reloadGame(){
        _gameState.postValue(GameState.Pre)
    }

    private fun nextWordPair() {
        currentWordPair = repo.getRandomWordPair()
        currentWordPair?.let {
            _gameState.postValue(GameState.Active(it))
        }
    }

    private fun loseALife() {
        if (currentLives > 0) {
            currentLives--
            _lives.postValue(currentLives)
        }
    }

    private fun gainAPoint() {
        currentScore++
        _score.postValue(currentScore)
    }
}