package com.haris.houlis.moviemaze

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.core.content.edit
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.haris.houlis.moviemaze.api.service.FakeApiService
import com.haris.houlis.moviemaze.api.service.TmdbService
import com.haris.houlis.moviemaze.data.db.DefaultMoviesDatabase
import com.haris.houlis.moviemaze.data.source.remote.UNKNOWN_ERROR_CODE
import com.haris.houlis.moviemaze.di.module.dataModule
import com.haris.houlis.moviemaze.di.module.movieDaosModule
import com.haris.houlis.moviemaze.ui.MainActivity
import com.haris.houlis.moviemaze.ui.browseMovies.MoviesFragment
import com.haris.houlis.moviemaze.ui.browseMovies.MoviesViewModel
import com.haris.houlis.moviemaze.ui.browseMovies.MoviesViewModel.Companion.KEY_QUERY
import com.haris.houlis.moviemaze.ui.handler.LoadStateErrorHandler
import com.haris.houlis.moviemaze.ui.manager.SharedPreferencesManager
import com.haris.houlis.moviemaze.ui.moviesDetails.MovieDetailsFragment
import com.haris.houlis.moviemaze.ui.moviesDetails.MovieDetailsViewModel
import com.haris.houlis.moviemaze.util.EspressoIdlingResource
import com.haris.houlis.moviemaze.util.isImeVisible
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertCustomAssertionAtPosition
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.clearText
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.pressImeActionButton
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import com.schibsted.spain.barista.internal.matcher.DrawableMatcher
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    private val app: TestApp = getApplicationContext()

    private lateinit var db: DefaultMoviesDatabase

    private lateinit var sharedPref: SharedPreferences

    private lateinit var prefManager: SharedPreferencesManager

    private val testFragmentsModule = module {
        scope<MoviesFragment> {
            scoped(override = true) { LoadStateErrorHandler(androidContext()) }
            viewModel(override = true) { (_: MainActivity) -> MoviesViewModel(prefManager, get()) }
        }

        scope<MovieDetailsFragment> {
            viewModel(override = true) { MovieDetailsViewModel(get()) }
        }
    }

    private val dbModule = module {
        single(override = true) { db }
    }

    private val baseModules = mutableListOf(
        dbModule, movieDaosModule, dataModule, testFragmentsModule
    )

    private fun getApiServiceModule(): Module = module {
        single<TmdbService>(override = true) { FakeApiService().apply { resetAllExceptions() } }
    }

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.simpleIdlingResource)

        val context = getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, DefaultMoviesDatabase::class.java)
            .build()

        val prefContext = getInstrumentation().context
        sharedPref = prefContext.getSharedPreferences("Test", Context.MODE_PRIVATE)
        prefManager = SharedPreferencesManager(sharedPref)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.simpleIdlingResource)

        db.close()
    }

    @Test
    fun onInitialFetch_apiReturnsTwoItems_RecyclerViewShowsTwoElements() {
        clearCaches()
        app.loadModule(baseModules.apply { add(getApiServiceModule()) }) {
            ActivityScenario.launch(MainActivity::class.java)

            sleep(100)
            assertRecyclerViewItemCount(R.id.movies_recyclerView, 2)
        }
    }

    @Test
    fun onTypeTextOnInputEditText_listUpdatesWithFourNewItems() {
        clearCaches()
        app.loadModule(baseModules.apply { add(getApiServiceModule()) }) {
            ActivityScenario.launch(MainActivity::class.java)

            clickOn(R.id.input_editText)
            writeTo(R.id.input_editText, "bat")
            pressImeActionButton()

            assertRecyclerViewItemCount(R.id.movies_recyclerView, 4)
        }
    }

    @Test
    fun whenSharedPrefHaveValue_inputEditTextHasThatValueAsText() {
        clearCaches()
        prefManager.write(KEY_QUERY, MoviesViewModel.SearchMovieQuery.Valid("bat"))
        app.loadModule(baseModules.apply { add(getApiServiceModule()) }) {
            val scenario = ActivityScenario.launch(MainActivity::class.java)

            assertContains(R.id.input_editText, "bat")
            scenario.onActivity { assert(!it.isImeVisible()) }
            assertRecyclerViewItemCount(R.id.movies_recyclerView, 4)
        }
    }

    @Test
    fun inputEditTextHasCachedValue_clearTextOfInputEditText_popularMoviesAreFetched() {
        clearCaches()
        prefManager.write(KEY_QUERY, MoviesViewModel.SearchMovieQuery.Valid("bat"))
        app.loadModule(baseModules.apply { add(getApiServiceModule()) }) {
            ActivityScenario.launch(MainActivity::class.java)

            assertContains(R.id.input_editText, "bat")
            clearText(R.id.input_editText)

            sleep(100)
            assertRecyclerViewItemCount(R.id.movies_recyclerView, 2)
        }
    }

    @Test
    fun apiCallReturnedWithError_toastIsShownAndListHasZeroItems() {
        clearCaches()
        val apiModule = module {
            single<TmdbService>(override = true) {
                FakeApiService().apply {
                    resetAllExceptions()
                    exception = IOException()
                }
            }
        }

        app.loadModule(baseModules.apply { add(apiModule) }) {
            lateinit var decorView: View
            ActivityScenario.launch(MainActivity::class.java).onActivity {
                decorView = it.window.decorView
            }

            val expectedToastText = getApplicationContext<Context>().getString(
                R.string.generic_error,
                UNKNOWN_ERROR_CODE
            )
            onView(withText(expectedToastText))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()))

            assertContains(R.id.input_editText, "")
            assertRecyclerViewItemCount(R.id.movies_recyclerView, 0)
        }
    }

    @Test
    fun movieHasValidRating_ratingBarIsShown() {
        clearCaches()
        app.loadModule(baseModules.apply { add(getApiServiceModule()) }) {
            ActivityScenario.launch(MainActivity::class.java)

            sleep(100)
            assertCustomAssertionAtPosition(
                R.id.movies_recyclerView,
                0,
                R.id.ratingBar,
                matches(isDisplayed())
            )
        }
    }

    @Test
    fun movieHasZeroVoteCount_NoRatingAvailableTextViewIsShownAndRatingBarIsGone() {
        clearCaches()
        app.loadModule(baseModules.apply { add(getApiServiceModule()) }) {
            ActivityScenario.launch(MainActivity::class.java)

            sleep(100)
            assertDisplayedAtPosition(
                R.id.movies_recyclerView,
                1,
                R.id.no_rating_textView,
                "No rating available"
            )
            assertCustomAssertionAtPosition(
                R.id.movies_recyclerView,
                1,
                R.id.ratingBar,
                matches(not(isDisplayed()))
            )
        }
    }

    @Test
    fun movieHasNullPosterPath_defaultPlaceholderDrawableIsShown() {
        clearCaches()
        app.loadModule(baseModules.apply { add(getApiServiceModule()) }) {
            ActivityScenario.launch(MainActivity::class.java)

            sleep(100)
            val hasDefaultPlaceholderDrawableMatcher = matches(
                anyOf(
                    hasDescendant(DrawableMatcher.withDrawable(R.drawable.movie_placeholder)),
                    DrawableMatcher.withDrawable(R.drawable.movie_placeholder)
                )
            )
            assertCustomAssertionAtPosition(
                R.id.movies_recyclerView,
                1,
                R.id.poster_imageView,
                hasDefaultPlaceholderDrawableMatcher
            )
        }
    }

    @Test
    fun movieHasValidPosterUrl_defaultPlaceholderDrawableIsNotShown() {
        clearCaches()
        app.loadModule(baseModules.apply { add(getApiServiceModule()) }) {
            ActivityScenario.launch(MainActivity::class.java)

            sleep(100)
            val defaultPlaceholderDrawableNotShown = matches(
                not(
                    anyOf(
                        hasDescendant(DrawableMatcher.withDrawable(R.drawable.movie_placeholder)),
                        DrawableMatcher.withDrawable(R.drawable.movie_placeholder)
                    )
                )
            )
            assertCustomAssertionAtPosition(
                R.id.movies_recyclerView,
                0,
                R.id.poster_imageView,
                defaultPlaceholderDrawableNotShown
            )
        }
    }

    @Test
    fun onMovieClick_detailsScreenIsShownSuccessfully() {
        clearCaches()
        app.loadModule(baseModules.apply { add(getApiServiceModule()) }) {
            ActivityScenario.launch(MainActivity::class.java)

            sleep(100)
            clickListItem(R.id.movies_recyclerView, 0)
            assertDisplayed(R.id.app_bar_layout)
        }
    }

    @Test
    fun onClickMovieWithValidReleaseDate_releaseDateTextViewInDetailsScreenHasProperText() {
        clearCaches()
        app.loadModule(baseModules.apply { add(getApiServiceModule()) }) {
            ActivityScenario.launch(MainActivity::class.java)

            sleep(100)
            clickListItem(R.id.movies_recyclerView, 0)

            assertContains(R.id.date_textView, "22 November 2014")
        }
    }

    @Test
    fun onClickMovieWithNullReleaseDate_releaseDateTextViewInDetailsScreenIsNotShown() {
        clearCaches()
        app.loadModule(baseModules.apply { add(getApiServiceModule()) }) {
            ActivityScenario.launch(MainActivity::class.java)

            sleep(100)
            clickListItem(R.id.movies_recyclerView, 1)

            assertNotDisplayed(R.id.date_textView)
        }
    }

    @Test
    fun onMovieClickWithValidReleaseDateAndRating_allDetailsFail_onlyRetryAndDateAndRatingViewsAreShown() {
        clearCaches()
        val apiModule = module {
            single<TmdbService>(override = true) {
                FakeApiService().apply {
                    resetAllExceptions()
                    basicDetailsException = IOException()
                }
            }
        }
        app.loadModule(baseModules.apply { add(apiModule) }) {
            ActivityScenario.launch(MainActivity::class.java)

            sleep(100)
            clickListItem(R.id.movies_recyclerView, 0)

            assertDisplayed(R.id.date_textView)
            assertDisplayed(R.id.ratingBar)

            assertNotDisplayed(R.id.genres_textView)
            assertNotDisplayed(R.id.overview_caption_textView)
            assertNotDisplayed(R.id.director_caption_textView)
            assertNotDisplayed(R.id.director_textView)
            assertNotDisplayed(R.id.cast_caption_textView)
            assertNotDisplayed(R.id.cast_textView)
            assertNotDisplayed(R.id.similarMovies_caption_textView)
            assertNotDisplayed(R.id.similarMovies_recyclerView)
            assertNotDisplayed(R.id.reviews_caption_textView)
            assertNotDisplayed(R.id.review1)
            assertNotDisplayed(R.id.review2)
            assertNotDisplayed(R.id.loading)

            assertDisplayed(R.id.emptyDetails_textView)
            assertDisplayed(R.id.retry_button)
        }
    }

    @Test
    fun onRetryButtonClick_contentUiIsBoundAndDisplayed() {
        clearCaches()
        val apiService = FakeApiService().apply {
            resetAllExceptions()
            basicDetailsException = IOException()
        }
        val apiModule = module {
            single<TmdbService>(override = true) {
                apiService
            }
        }
        app.loadModule(baseModules.apply { add(apiModule) }) {
            ActivityScenario.launch(MainActivity::class.java)

            clickListItem(R.id.movies_recyclerView, 0)

            assertDisplayed(R.id.date_textView)
            assertDisplayed(R.id.ratingBar)

            assertDisplayed(R.id.emptyDetails_textView)
            assertDisplayed(R.id.retry_button)

            apiService.resetAllExceptions()

            clickOn(R.id.retry_button)

            assertDisplayed(R.id.date_textView)
            assertDisplayed(R.id.ratingBar)
            assertDisplayed(R.id.genres_textView)
            assertDisplayed(R.id.overview_caption_textView)
            assertDisplayed(R.id.director_caption_textView)
            assertDisplayed(R.id.director_textView)
            assertDisplayed(R.id.cast_caption_textView)
            assertDisplayed(R.id.cast_textView)
            assertDisplayed(R.id.similarMovies_caption_textView)
            assertDisplayed(R.id.similarMovies_recyclerView)

            assertDisplayed(R.id.reviews_caption_textView)
            assertDisplayed(R.id.review1)
            assertNotDisplayed(R.id.review2)

            assertNotDisplayed(R.id.loading)
            assertNotDisplayed(R.id.emptyDetails_textView)
            assertNotDisplayed(R.id.retry_button)
        }
    }

    @Test
    fun onMovieClickWithValidReleaseDateAndRating_reviewsFetchFails_errorAndReviewsLayoutsAreNotShown() {
        clearCaches()
        val apiModule = module {
            single<TmdbService>(override = true) {
                FakeApiService().apply {
                    resetAllExceptions()
                    reviewsException = IOException()
                }
            }
        }
        app.loadModule(baseModules.apply { add(apiModule) }) {
            ActivityScenario.launch(MainActivity::class.java)

            sleep(100)
            clickListItem(R.id.movies_recyclerView, 0)

            assertDisplayed(R.id.date_textView)
            assertDisplayed(R.id.ratingBar)
            assertDisplayed(R.id.genres_textView)
            assertDisplayed(R.id.overview_caption_textView)
            assertDisplayed(R.id.director_caption_textView)
            assertDisplayed(R.id.director_textView)
            assertDisplayed(R.id.cast_caption_textView)
            assertDisplayed(R.id.cast_textView)
            assertDisplayed(R.id.similarMovies_caption_textView)
            assertDisplayed(R.id.similarMovies_recyclerView)

            assertNotDisplayed(R.id.reviews_caption_textView)
            assertNotDisplayed(R.id.review1)
            assertNotDisplayed(R.id.review2)

            assertNotDisplayed(R.id.loading)
            assertNotDisplayed(R.id.emptyDetails_textView)
            assertNotDisplayed(R.id.retry_button)
        }
    }

    @Test
    fun onMovieClickWithValidReleaseDateAndRating_similarMoviesFetchFails_errorAndSimilarMoviesLayoutsAreNotShown() {
        clearCaches()
        val apiModule = module {
            single<TmdbService>(override = true) {
                FakeApiService().apply {
                    resetAllExceptions()
                    similarException = IOException()
                }
            }
        }
        app.loadModule(baseModules.apply { add(apiModule) }) {
            ActivityScenario.launch(MainActivity::class.java)

            sleep(100)
            clickListItem(R.id.movies_recyclerView, 0)

            assertDisplayed(R.id.date_textView)
            assertDisplayed(R.id.ratingBar)
            assertDisplayed(R.id.genres_textView)
            assertDisplayed(R.id.overview_caption_textView)
            assertDisplayed(R.id.director_caption_textView)
            assertDisplayed(R.id.director_textView)
            assertDisplayed(R.id.cast_caption_textView)
            assertDisplayed(R.id.cast_textView)

            assertNotDisplayed(R.id.similarMovies_caption_textView)
            assertNotDisplayed(R.id.similarMovies_recyclerView)

            assertDisplayed(R.id.reviews_caption_textView)
            assertDisplayed(R.id.review1)
            assertNotDisplayed(R.id.review2)

            assertNotDisplayed(R.id.loading)
            assertNotDisplayed(R.id.emptyDetails_textView)
            assertNotDisplayed(R.id.retry_button)
        }
    }

    @Test
    fun onClickMovieWithTwoSimilarMovies_similarMoviesRecyclerViewHasTwoVisibleItems() {
        clearCaches()
        app.loadModule(baseModules.apply { add(getApiServiceModule()) }) {
            ActivityScenario.launch(MainActivity::class.java)

            sleep(100)
            clickListItem(R.id.movies_recyclerView, 0)

            assertDisplayed(R.id.similarMovies_recyclerView)
            assertRecyclerViewItemCount(R.id.similarMovies_recyclerView, 2)
            assertCustomAssertionAtPosition(
                listId = R.id.similarMovies_recyclerView,
                position = 0,
                viewAssertion = matches(isDisplayed())
            )
            assertCustomAssertionAtPosition(
                listId = R.id.similarMovies_recyclerView,
                position = 1,
                viewAssertion = matches(isDisplayed())
            )
        }
    }

    private fun FakeApiService.resetAllExceptions() {
        exception = null
        basicDetailsException = null
        stakeholdersException = null
        reviewsException = null
        similarException = null
    }

    private fun clearCaches() {
        cleanPref()
        cleanDb()
    }

    private fun cleanDb() = runBlocking {
        db.moviesDao().clearMovies()
        db.remoteKeysDao().clearRemoteKeys()
    }

    private fun cleanPref() =
        sharedPref.edit {
            clear()
            apply()
        }
}