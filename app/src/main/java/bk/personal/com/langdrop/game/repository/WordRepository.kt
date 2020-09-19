package bk.personal.com.langdrop.game.repository

import bk.personal.com.langdrop.model.GameWordPair
import bk.personal.com.langdrop.model.Word
import bk.personal.com.langdrop.utils.IRandomNumberGenerator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

interface IWordRepository {
    fun getRandomWordPair(): GameWordPair?
}

class WordRepository @Inject constructor(
    private val wordJson: String,
    private val gson: Gson,
    private val randomGenerator: IRandomNumberGenerator
) : IWordRepository {

    private var wordList: List<Word>? = null

    init {
        convertJsonStringToWordObjects()
    }

    private fun convertJsonStringToWordObjects() {
        val turnsType = object : TypeToken<List<Word>>() {}.type
        val words = gson.fromJson<List<Word>>(wordJson, turnsType)
        wordList = words
    }

    override fun getRandomWordPair(): GameWordPair? {
        wordList?.let {
            val w = it[randomGenerator.generateRandomNumberInclusive(0, (it.size - 1))]
            return if (randomGenerator.shouldOtherWordBeCorrect()) {
                GameWordPair(w.text_eng, w.text_spa, true)
            } else {
                val falseWord = it[randomGenerator.generateRandomNumberInclusive(0, (it.size - 1))]
                GameWordPair(w.text_eng, falseWord.text_spa, false)
            }
        } ?: return null
    }
}