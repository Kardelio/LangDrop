package bk.personal.com.langdrop.game.repository

import bk.personal.com.langdrop.di.AppModule
import bk.personal.com.langdrop.utils.IRandomNumberGenerator
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import testingUtils.anyInt
import testingUtils.classMock
import testingUtils.whenever

class WordRepositoryTest {

    private lateinit var repo: WordRepository

    private val gsonMock: Gson = AppModule.provideGson()
    private val randMock: IRandomNumberGenerator = classMock()

    @Before
    fun setup() {
    }

    @Test
    fun `test get random word works correctly`() {
        repo = WordRepository(WORD_JSON_ONE, gsonMock, randMock)
        whenever(randMock.generateRandomNumberInclusive(anyInt(), anyInt())).thenReturn(0)
        whenever(randMock.shouldOtherWordBeCorrect()).thenReturn(true)

        val wordPair = repo.getRandomWordPair()
        assertNotNull(wordPair)
        wordPair?.let {
            assertEquals("test", wordPair.eng)
            assertTrue(wordPair.correct)
        }
    }

    @Test
    fun `test get random word works correctly more than one word returns correct pair`() {
        repo = WordRepository(WORD_JSON_TWO, gsonMock, randMock)
        whenever(randMock.generateRandomNumberInclusive(anyInt(), anyInt())).thenReturn(1)
        whenever(randMock.shouldOtherWordBeCorrect()).thenReturn(true)

        val wordPair = repo.getRandomWordPair()
        assertNotNull(wordPair)
        wordPair?.let {
            assertEquals("test2", wordPair.eng)
            assertTrue(wordPair.correct)
        }
    }

    @Test
    fun `test get random word works correctly more than one word returns wrong pair`() {
        repo = WordRepository(WORD_JSON_TWO, gsonMock, randMock)
        whenever(randMock.generateRandomNumberInclusive(anyInt(), anyInt())).thenReturn(1)
        whenever(randMock.shouldOtherWordBeCorrect()).thenReturn(false)

        val wordPair = repo.getRandomWordPair()
        assertNotNull(wordPair)
        wordPair?.let {
            assertEquals("test2", wordPair.eng)
            assertFalse(wordPair.correct)
        }
    }

    @Test
    fun `test get random word with empty json data returns null`() {
        repo = WordRepository("", gsonMock, randMock)
        whenever(randMock.generateRandomNumberInclusive(anyInt(), anyInt())).thenReturn(1)

        val wordPair = repo.getRandomWordPair()
        assertNull(wordPair)
    }

    companion object {
        val WORD_JSON_ONE = """
            [
              {
                "text_eng": "test",
                "text_spa": "test_spa"
              },
            ]
        """.trimIndent()

        val WORD_JSON_TWO = """
            [
              {
                "text_eng": "test1",
                "text_spa": "test_spa1"
              },
              {
                "text_eng": "test2",
                "text_spa": "test_spa2"
              },
              {
               "text_eng": "test3",
               "text_spa": "test_spa3"
              },
            ]
        """.trimIndent()
    }
}