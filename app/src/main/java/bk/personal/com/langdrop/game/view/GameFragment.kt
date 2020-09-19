package bk.personal.com.langdrop.game.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bk.personal.com.langdrop.R
import bk.personal.com.langdrop.game.viewmodel.GameViewModel
import bk.personal.com.langdrop.model.Word
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {

    private val viewmodel: GameViewModel by viewModels()

    private lateinit var tv: TextView
    private lateinit var b: Button
    private lateinit var bs: Button
    private lateinit var m: MotionLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_game, container, false)
        tv = v.findViewById<TextView>(R.id.tv)
        m = v.findViewById(R.id.ml)
        b = v.findViewById<Button>(R.id.button2)
        bs = v.findViewById(R.id.bb)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO observe things
        m.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                Log.d("BK", "DONEEEE")
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }
        })
//        m.transitionToEnd()
//        m.progress = 0.5f
        m.setTransitionDuration(10000)
        b.setOnClickListener {
            Log.d("BK", "Stop!")
            m.setProgress(0f, 0f)
        }
        bs.setOnClickListener {
            m.transitionToEnd()
        }
        viewmodel.test()
//        close()
    }

    fun close(){
        requireActivity().finish()
    }

}
