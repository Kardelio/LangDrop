package bk.personal.com.langdrop.utils

import javax.inject.Inject

interface IRandomNumberGenerator {
    fun generateRandomNumberInclusive(from: Int, to: Int): Int
    fun shouldOtherWordBeCorrect(): Boolean
}

class RandomNumberGenerator @Inject constructor(): IRandomNumberGenerator {
    /**
     * generated random from 'from' to 'to' inclusive , 5 to 8 includes 5,6,7,8
     */
    override fun generateRandomNumberInclusive(from: Int, to: Int): Int {
        return (from..to).random()
    }

    /**
     * This function provides a sort of clip flip
     * a 50/50 chance that the app will show the user
     * a correct answer or false answer for each question
     */
    override fun shouldOtherWordBeCorrect(): Boolean {
        return (0..1).random() == 0
    }
}