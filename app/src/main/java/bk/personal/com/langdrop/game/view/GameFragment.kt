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

    private lateinit var tv: TextView
    private lateinit var m: MotionLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_game, container, false)
        tv = v.findViewById<TextView>(R.id.tv)
        m = v.findViewById(R.id.ml)

        startButton = v.findViewById(R.id.start_screen_play_button)
        closeButton = v.findViewById(R.id.start_screen_close_button)
        wrongButton = v.findViewById(R.id.wrong_button)
        correctButton = v.findViewById(R.id.correct_button)
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
            m.setTransitionDuration(500)
            m.addTransitionListener(playScreenTransitionListerner)
            m.transitionToState(R.id.preStartEnd)
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
        m.setTransition(R.id.start, R.id.end)
        m.removeTransitionListener(playScreenTransitionListerner)
        m.removeTransitionListener(wordDropTransitionListerner)
        m.addTransitionListener(wordDropTransitionListerner)
        m.setTransitionDuration(2000)

        loadActiveWordPair(gameState.activePair)
        m.transitionToEnd()
    }

    private fun loadPreGameView() {

    }

    private fun loadPostGameView() {
        Log.d("BENK","Game Over!")
    }

    private fun loadActiveWordPair(wordPair: GameWordPair) {
        tv.text = wordPair.eng
    }

    fun closeApp() {
        requireActivity().finish()
    }
}
