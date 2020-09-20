package bk.personal.com.langdrop.game.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import bk.personal.com.langdrop.R
import bk.personal.com.langdrop.game.viewmodel.GameState
import bk.personal.com.langdrop.game.viewmodel.GameViewModel
import bk.personal.com.langdrop.model.GameWordPair
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import testingUtils.classMock
import testingUtils.launchFragmentInHiltContainer
import testingUtils.replace

@HiltAndroidTest
class GameFragmentTest {

    private val mockViewModel: GameViewModel = classMock()
    //livedatas here

    private val score: MutableLiveData<Int> = MutableLiveData()
    private val lives: MutableLiveData<Int> = MutableLiveData()
    private val gameState: MutableLiveData<GameState> = MutableLiveData()

    private val sampleWordPair = GameWordPair("a", "b", true)

    @get:Rule
    val rule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private fun launchFragment() {
        launchFragmentInHiltContainer<GameFragment> {
            this.replace(GameFragment::viewmodel, mockViewModel)
        }
    }

    @Before
    fun setup() {
        rule.inject()
        mockViewModel.stub {
            on { lives } doReturn lives
            on { score } doReturn score
            on { gameState } doReturn gameState
        }
    }

    @Test
    fun test_game_state_pre_view_starting_state_correct() {
        gameState.postValue(GameState.Pre)
        launchFragment()

        onView(withId(R.id.start_screen_play_button)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.start_screen_close_button)).check(matches(isCompletelyDisplayed()))

        onView(withId(R.id.over_screen_button)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_game_state_over_view_starting_state_correct() {
        gameState.postValue(GameState.Over)
        launchFragment()
        onView(withId(R.id.over_screen_button)).check(matches(isCompletelyDisplayed()))

        onView(withId(R.id.start_screen_play_button)).check(matches(not(isDisplayed())))
        onView(withId(R.id.start_screen_close_button)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_game_state_active_view_starting_state_correct() {
        gameState.postValue(GameState.Active(sampleWordPair))
        launchFragment()
        Thread.sleep(5000)

    }
}
