package bk.personal.com.langdrop.game.repository

import android.util.Log
import bk.personal.com.langdrop.R
import bk.personal.com.langdrop.model.Word
import bk.personal.com.langdrop.utils.IRandomNumberGenerator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

interface IWordRepository {
    fun getAllWords(): List<Word>
}

class WordRepository @Inject constructor(
    private val wordJson: String,
    private val gson: Gson,
    randNumGen: IRandomNumberGenerator
) : IWordRepository {

    init {
        read()

//        val ans = randNumGen.generateRandomNumberInclusive(5,8)
//        Log.d("BENK","${ans.toString()}")
    }

    fun read() {
        val turnsType = object : TypeToken<List<Word>>() {}.type
        val words = gson.fromJson<List<Word>>(wordJson, turnsType)
        Log.d("BENK", "${words.size}")
    }

    override fun getAllWords(): List<Word> {
        return emptyList()
    }
}