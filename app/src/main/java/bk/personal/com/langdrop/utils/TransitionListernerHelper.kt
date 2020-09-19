package bk.personal.com.langdrop.utils

import android.animation.Animator
import androidx.constraintlayout.motion.widget.MotionLayout




inline fun TransitionHelper(crossinline continuation: () -> Unit): MotionLayout.TransitionListener{
    val a = object : MotionLayout.TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        }
        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        }
        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            continuation()
        }
        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
        }
    }
    return a
}
