package bk.personal.com.langdrop.model

data class Word(
    val text_eng: String = "",
    val text_spa: String = ""
)

data class GameWordPair(
    val eng: String = "",
    val spa: String = "",
    val correct: Boolean = false
)