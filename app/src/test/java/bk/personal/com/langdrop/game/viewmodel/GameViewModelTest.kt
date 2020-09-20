package bk.personal.com.langdrop.game.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import bk.personal.com.langdrop.game.repository.IWordRepository
import bk.personal.com.langdrop.model.GameWordPair
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import testingUtils.argumentCaptor
import testingUtils.capture
import testingUtils.classMock
import testingUtils.whenever

class GameViewModelTest {

    private lateinit var viewmodel: GameViewModel

    private val repo: IWordRepository = classMock()

    private val scoreObserver: Observer<Int> = classMock()
    private val scoreCaptor: ArgumentCaptor<Int> = argumentCaptor()

    private val livesObserver: Observer<Int> = classMock()
    private val livesCaptor: ArgumentCaptor<Int> = argumentCaptor()

    private val gameStateObserver: Observer<GameState> = classMock()
    private val gameStateCaptor: ArgumentCaptor<GameState> = argumentCaptor()

    private val randomWordPairRight = GameWordPair("a", "b", true)
    private val randomWordPairWrong = GameWordPair("b", "a", false)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewmodel = GameViewModel(repo)
        viewmodel.score.observeForever(scoreObserver)
        viewmodel.lives.observeForever(livesObserver)
        viewmodel.gameState.observeForever(gameStateObserver)
    }

    @Test
    fun `test viewmodel initialised with Pre game state`() {
        verify(gameStateObserver).onChanged(capture(gameStateCaptor))
        assertTrue(gameStateCaptor.value is GameState.Pre)
    }

    @Test
    fun `test start game sets the lives and score and gets first random word pair`() {
        whenever(repo.getRandomWordPair()).thenReturn(randomWordPairRight)

        viewmodel.startGame()

        verify(scoreObserver).onChanged(capture(scoreCaptor))
        assertEquals(0, scoreCaptor.value)
        verify(livesObserver).onChanged(capture(livesCaptor))
        assertEquals(3, livesCaptor.value)

        verify(repo).getRandomWordPair()
        verify(gameStateObserver, atLeast(2)).onChanged(capture(gameStateCaptor))
        assertTrue(gameStateCaptor.allValues.last() is GameState.Active)
        val gs = (gameStateCaptor.allValues.last() as GameState.Active)
        assertEquals(gs.activePair.eng, "a")
    }

    @Test
    fun `test player let word fall all the way lives drop`() {
        viewmodel.submittedAnswer(null)
        verify(livesObserver).onChanged(capture(livesCaptor))
        assertEquals(2, livesCaptor.value)
    }

    @Test
    fun `test player gets answer wrong lives drop`() {
        whenever(repo.getRandomWordPair()).thenReturn(randomWordPairRight)

        viewmodel.startGame()
        viewmodel.submittedAnswer(false)

        verify(livesObserver, atLeastOnce()).onChanged(capture(livesCaptor))
        assertEquals(2, livesCaptor.value)
    }

    @Test
    fun `test player gets answer right score increase`() {
        whenever(repo.getRandomWordPair()).thenReturn(randomWordPairRight)

        viewmodel.startGame()
        viewmodel.submittedAnswer(true)

        verify(livesObserver).onChanged(capture(livesCaptor))
        assertEquals(3, livesCaptor.value)

        verify(scoreObserver, atLeastOnce()).onChanged(capture(scoreCaptor))
        assertEquals(1, scoreCaptor.value)
    }

    @Test
    fun `test user answers question gets another word`(){
        whenever(repo.getRandomWordPair()).thenReturn(randomWordPairRight)

        viewmodel.startGame()

        whenever(repo.getRandomWordPair()).thenReturn(randomWordPairWrong)

        viewmodel.submittedAnswer(true)

        verify(repo, times(2)).getRandomWordPair()
        verify(gameStateObserver, times(3)).onChanged(capture(gameStateCaptor))
        assertTrue(gameStateCaptor.allValues.last() is GameState.Active)
        val gs = (gameStateCaptor.allValues.last() as GameState.Active)
        assertEquals(gs.activePair.eng, "b")
        assertEquals(gs.activePair.spa, "a")
        assertFalse(gs.activePair.correct)
    }

    @Test
    fun `test user loses all lives and game over triggers`(){
        whenever(repo.getRandomWordPair()).thenReturn(randomWordPairRight)
        viewmodel.startGame()
        viewmodel.submittedAnswer(false)
        viewmodel.submittedAnswer(false)
        viewmodel.submittedAnswer(false)
        //Pre, Active, Active, Active, Over
        verify(gameStateObserver, times(5)).onChanged(capture(gameStateCaptor))
        assertTrue(gameStateCaptor.allValues.last() is GameState.Over)
    }
}