package bk.personal.com.langdrop.game.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import bk.personal.com.langdrop.R
import bk.personal.com.langdrop.game.viewmodel.GameState
import bk.personal.com.langdrop.game.viewmodel.GameViewModel
import bk.personal.com.langdrop.model.GameWordPair
import bk.personal.com.langdrop.utils.TransitionHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {

    private val viewmodel: GameViewModel by viewModels()

    private lateinit var startButton: Button
    private lateinit var closeButton: Button
    private lateinit var correctButton: Button
    private lateinit var wrongButton: Button

    private lateinit var fallingText: TextView
    private lateinit var staticText: TextView
    private lateinit var playerScore: TextView
    private lateinit var playerLives: TextView
    private lateinit var motionLayout: MotionLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_game, container, false)
        startButton = v.findViewById(R.id.start_screen_play_button)
        closeButton = v.findViewById(R.id.start_screen_close_button)
        wrongButton = v.findViewById(R.id.wrong_button)
        correctButton = v.findViewById(R.id.correct_button)
        fallingText = v.findViewById(R.id.falling_text)
        staticText = v.findViewById(R.id.static_text)
        playerLives = v.findViewById(R.id.player_lives)
        playerScore = v.findViewById(R.id.player_score)
        motionLayout = v.findViewById(R.id.game_motion_layout)
        return v
    }

    private val wordDropTransitionListerner = TransitionHelper {
        viewmodel.submittedAnswer(null)
    }

    private val playScreenTransitionListerner = TransitionHelper {
        viewmodel.startGame()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startButton.setOnClickListener {
            motionLayout.setTransitionDuration(500)
            motionLayout.addTransitionListener(playScreenTransitionListerner)
            motionLayout.transitionToState(R.id.preStartEnd)
        }

        closeButton.setOnClickListener {
            closeApp()
        }

        correctButton.setOnClickListener {
            viewmodel.submittedAnswer(true)
        }

        wrongButton.setOnClickListener {
            viewmodel.submittedAnswer(false)
        }

        viewmodel.gameState.observe(viewLifecycleOwner, Observer {
            renderGameState(it)
        })

        viewmodel.score.observe(viewLifecycleOwner, Observer {
            playerScore.text = "$it"
        })

        viewmodel.lives.observe(viewLifecycleOwner, Observer {
            playerLives.text = "$it"
        })
    }

    private fun renderGameState(gameState: GameState) {
        when (gameState) {
            is GameState.Pre -> {
                loadPreGameView()
            }
            is GameState.Over -> {
                loadPostGameView()
            }
            is GameState.Active -> {
                loadActiveGameView(gameState)
            }
        }
    }

    private fun loadActiveGameView(gameState: GameState.Active) {
        motionLayout.setTransition(R.id.start, R.id.end)
        motionLayout.removeTransitionListener(playScreenTransitionListerner)
        motionLayout.removeTransitionListener(wordDropTransitionListerner)
        motionLayout.addTransitionListener(wordDropTransitionListerner)
        motionLayout.setTransitionDuration(GAME_SPEED)

        loadActiveWordPair(gameState.activePair)
        motionLayout.transitionToEnd()
    }

    private fun loadPreGameView() {

    }

    private fun loadPostGameView() {
        Log.d("BENK","Game Over!")
    }

    private fun loadActiveWordPair(wordPair: GameWordPair) {
        fallingText.text = wordPair.eng
        staticText.text = wordPair.spa
    }

    private fun closeApp() {
        requireActivity().finish()
    }

    companion object{
        const val GAME_SPEED = 10000 //10 seconds for word to fall
    }
}
