package bk.personal.com.langdrop.game.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import bk.personal.com.langdrop.R
import bk.personal.com.langdrop.game.viewmodel.GameState
import bk.personal.com.langdrop.game.viewmodel.GameViewModel
import bk.personal.com.langdrop.model.GameWordPair
import bk.personal.com.langdrop.utils.transitionHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {

    private val viewmodel: GameViewModel by viewModels()

    private lateinit var motionLayout: MotionLayout

    private lateinit var startButton: Button
    private lateinit var closeButton: Button

    private lateinit var correctButton: Button
    private lateinit var wrongButton: Button
    private lateinit var fallingText: TextView
    private lateinit var staticText: TextView
    private lateinit var playerScore: TextView
    private lateinit var playerLives: TextView

    private lateinit var overButton: Button
    private lateinit var overScoreText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_game, container, false)
        motionLayout = v.findViewById(R.id.game_motion_layout)

        startButton = v.findViewById(R.id.start_screen_play_button)
        closeButton = v.findViewById(R.id.start_screen_close_button)

        wrongButton = v.findViewById(R.id.wrong_button)
        correctButton = v.findViewById(R.id.correct_button)
        fallingText = v.findViewById(R.id.falling_text)
        staticText = v.findViewById(R.id.static_text)
        playerLives = v.findViewById(R.id.player_lives)
        playerScore = v.findViewById(R.id.player_score)

        overButton = v.findViewById(R.id.over_screen_button)
        overScoreText = v.findViewById(R.id.over_screen_score_text)
        return v
    }

    private val menuScreenToActiveTransitionListerner = transitionHelper {
        //After user clicked play once the animation has finished the game
        //itself will begin
        viewmodel.startGame()
    }

    private val wordDropTransitionListerner = transitionHelper {
        //if the falling word hits the bottom of the screen
        //before the user interacts with the buttons
        viewmodel.submittedAnswer(null)
    }

//    private val activeScreenToGameOverScreenListerner = transitionHelper {
////        viewmodel.startGame()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startButton.setOnClickListener {
            motionLayout.setTransitionDuration(500)
            motionLayout.addTransitionListener(menuScreenToActiveTransitionListerner)
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

        overButton.setOnClickListener {
            viewmodel.reloadGame()
        }

        viewmodel.gameState.observe(viewLifecycleOwner, Observer {
            renderGameState(it)
        })

        viewmodel.score.observe(viewLifecycleOwner, Observer {
            playerScore.text = "$it"
            overScoreText.text = "$it"
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
        motionLayout.removeTransitionListener(menuScreenToActiveTransitionListerner)
        motionLayout.removeTransitionListener(wordDropTransitionListerner)
        motionLayout.addTransitionListener(wordDropTransitionListerner)
        motionLayout.setTransitionDuration(GAME_SPEED)

        loadActiveWordPair(gameState.activePair)
        motionLayout.transitionToEnd()
    }

    private fun loadPreGameView() {
        motionLayout.setTransition(R.id.preStartEnd, R.id.preStart)
        motionLayout.setTransitionDuration(0)
        motionLayout.progress = 0f
        motionLayout.transitionToEnd()
    }

    private fun loadPostGameView() {
        motionLayout.removeTransitionListener(wordDropTransitionListerner)
        motionLayout.progress = 0f
        motionLayout.setTransitionDuration(1000)
        motionLayout.transitionToState(R.id.overEnd)
    }

    private fun loadActiveWordPair(wordPair: GameWordPair) {
        fallingText.text = wordPair.eng
        staticText.text = wordPair.spa
    }

    private fun closeApp() {
        requireActivity().finish()
    }

    companion object{
        const val GAME_SPEED = 1000 //10 seconds for word to fall
    }
}
