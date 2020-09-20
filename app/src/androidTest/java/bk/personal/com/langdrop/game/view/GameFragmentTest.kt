package bk.personal.com.langdrop.game.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import bk.personal.com.langdrop.R
import bk.personal.com.langdrop.game.viewmodel.GameState
import bk.personal.com.langdrop.game.viewmodel.GameViewModel
import bk.personal.com.langdrop.model.GameWordPair
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verify
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import testingUtils.*
import testingUtils.ImageViewMatcher.Companion.nthChildOf

@HiltAndroidTest
class GameFragmentTest {
    private val mockViewModel: GameViewModel = classMock()

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
            on { gameSpeed } doReturn 500
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
        onView(withId(R.id.correct_button)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.wrong_button)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun test_pre_game_play_button_pressed() {
        gameState.postValue(GameState.Pre)
        launchFragment()

        onView(withId(R.id.start_screen_play_button)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.start_screen_play_button)).perform(click())
        verify(mockViewModel).startGame()
    }

    @Test
    fun test_over_screen_button_is_pressed() {
        gameState.postValue(GameState.Over)
        launchFragment()

        onView(withId(R.id.over_screen_button)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.over_screen_button)).perform(click())
        verify(mockViewModel).reloadGame()
    }

    @Test
    fun test_active_screen_correct_button_is_pressed() {
        gameState.postValue(GameState.Active(sampleWordPair))
        launchFragment()
        onView(withId(R.id.correct_button)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.correct_button)).perform(click())
        verify(mockViewModel).submittedAnswer(eq(true))
    }

    @Test
    fun test_active_screen_wrong_button_is_pressed() {
        gameState.postValue(GameState.Active(sampleWordPair))
        launchFragment()
        onView(withId(R.id.wrong_button)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.wrong_button)).perform(click())
        verify(mockViewModel).submittedAnswer(eq(false))
    }

    @Test
    fun test_active_screen_word_falls_all_the_way_to_the_bottom() {
        gameState.postValue(GameState.Active(sampleWordPair))
        launchFragment()
        Thread.sleep(1000)
        verify(mockViewModel).submittedAnswer(eq(null))
    }

    @Test
    fun test_words_from_pair_display_correctly(){
        gameState.postValue(GameState.Active(sampleWordPair))
        launchFragment()
        onView(withId(R.id.static_text)).check(matches(allOf(isCompletelyDisplayed(), withText("a"))))
        onView(withId(R.id.falling_text)).check(matches(withText("b")))
    }

    @Test
    fun test_score_updates_correctly() {
        gameState.postValue(GameState.Active(sampleWordPair))
        score.postValue(2)
        launchFragment()
        onView(withId(R.id.player_score)).check(matches(withText("Score: 2")))
    }

    @Test
    fun test_three_lives_updates_correctly() {
        gameState.postValue(GameState.Active(sampleWordPair))
        lives.postValue(3)
        launchFragment()
        //Always 3 hearts but some may be not filled in
        onView(withId(R.id.lives_area)).check(matches(hasChildCount(3)))
        onView(
            nthChildOf(
                withId(R.id.lives_area),
                0
            )
        ).check(matches(ImageViewMatcher.withDrawable(R.drawable.ic_heart_filled)))
        onView(
            nthChildOf(
                withId(R.id.lives_area),
                1
            )
        ).check(matches(ImageViewMatcher.withDrawable(R.drawable.ic_heart_filled)))
        onView(
            nthChildOf(
                withId(R.id.lives_area),
                2
            )
        ).check(matches(ImageViewMatcher.withDrawable(R.drawable.ic_heart_filled)))
    }

    @Test
    fun test_no_lives_updates_correctly() {
        gameState.postValue(GameState.Active(sampleWordPair))
        lives.postValue(0)
        launchFragment()
        //Always 3 hearts but some may be not filled in
        onView(withId(R.id.lives_area)).check(matches(hasChildCount(3)))
        onView(
            nthChildOf(
                withId(R.id.lives_area),
                0
            )
        ).check(matches(ImageViewMatcher.withDrawable(R.drawable.ic_heart_empty)))
        onView(
            nthChildOf(
                withId(R.id.lives_area),
                1
            )
        ).check(matches(ImageViewMatcher.withDrawable(R.drawable.ic_heart_empty)))
        onView(
            nthChildOf(
                withId(R.id.lives_area),
                2
            )
        ).check(matches(ImageViewMatcher.withDrawable(R.drawable.ic_heart_empty)))
    }

    @Test
    fun test_two_lives_updates_correctly() {
        gameState.postValue(GameState.Active(sampleWordPair))
        lives.postValue(2)
        launchFragment()
        //Always 3 hearts but some may be not filled in
        onView(withId(R.id.lives_area)).check(matches(hasChildCount(3)))
        onView(
            nthChildOf(
                withId(R.id.lives_area),
                0
            )
        ).check(matches(ImageViewMatcher.withDrawable(R.drawable.ic_heart_filled)))
        onView(
            nthChildOf(
                withId(R.id.lives_area),
                1
            )
        ).check(matches(ImageViewMatcher.withDrawable(R.drawable.ic_heart_filled)))
        onView(
            nthChildOf(
                withId(R.id.lives_area),
                2
            )
        ).check(matches(ImageViewMatcher.withDrawable(R.drawable.ic_heart_empty)))
    }
}
