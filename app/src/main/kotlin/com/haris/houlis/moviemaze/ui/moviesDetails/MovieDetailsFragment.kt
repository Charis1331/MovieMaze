package com.haris.houlis.moviemaze.ui.moviesDetails

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.transition.MaterialContainerTransform
import com.haris.houlis.moviemaze.R
import com.haris.houlis.moviemaze.data.vo.*
import com.haris.houlis.moviemaze.data.vo.Movie.Companion.drawableResId
import com.haris.houlis.moviemaze.databinding.FragmentMovieDetailsBinding
import com.haris.houlis.moviemaze.databinding.IncludeReviewLayoutBinding
import com.haris.houlis.moviemaze.extensions.fitToSystemWindows
import com.haris.houlis.moviemaze.extensions.gone
import com.haris.houlis.moviemaze.extensions.statusBarColor
import com.haris.houlis.moviemaze.extensions.visible
import com.haris.houlis.moviemaze.ui.manager.ToastManager
import com.haris.houlis.moviemaze.util.EspressoIdlingResource
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.list_item_movie.*
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsFragment : ScopeFragment() {

    private val viewModel: MovieDetailsViewModel by viewModel()

    private lateinit var movie: Movie
    private var transitionName: String = ""

    private val adapter = SimilarMoviesAdapter()
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSharedElementTransition()

        retrieveArgs()

        setHasOptionsMenu(true)
    }

    private fun setupSharedElementTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = Color.TRANSPARENT
        }
    }

    private fun retrieveArgs() {
        val args = arguments
        if (args != null) {
            args.getParamsOrExitIfNull()
        } else {
            showErrorMessageAndExit()
        }
    }

    private fun Bundle.getParamsOrExitIfNull() {
        val movieParam = getParcelable<Movie>(MOVIE_PARAM)
        val transitionNameParam = getString(TRANSITION_NAME_PARAM)
        if (movieParam == null || transitionNameParam.isNullOrEmpty()) {
            showErrorMessageAndExit()
            return
        }
        movie = movieParam
        transitionName = transitionNameParam
    }

    private fun showErrorMessageAndExit() {
        val message = getString(R.string.something_went_wrong)
        showToast(message)
        requireActivity().onBackPressed()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        setupUi()

        registerObservers()

        fetchDetails()
    }

    private fun drawBehindStatusBar() {
        requireActivity().statusBarColor(android.R.color.transparent)

        with(binding) {
            root.fitToSystemWindows(toolbar.toolbar)
        }
    }

    private fun setupUi() {
        drawBehindStatusBar()

        setupActionBar()

        with(binding) {
            setupRecyclerView()
            setupRetryClickListener()
            bindTitle()
            bindPoster()
            bindFavorite(movie.favorite)
            bindReleaseDate()
            bindRatingBar()
        }

    }

    private fun setupActionBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(binding.toolbar.toolbar)
            supportActionBar?.run {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    private fun FragmentMovieDetailsBinding.setupRecyclerView() {
        with(similarMoviesRecyclerView) {
            setHasFixedSize(true)
            adapter = this@MovieDetailsFragment.adapter
        }
    }

    private fun FragmentMovieDetailsBinding.setupRetryClickListener() {
        retryButton.setOnClickListener {
            fetchDetails()
        }
    }

    private fun FragmentMovieDetailsBinding.bindTitle() {
        collapsingToolbar.title = movie.title
    }

    private fun FragmentMovieDetailsBinding.bindPoster() {
        posterImageView.transitionName = this@MovieDetailsFragment.transitionName
        Glide.with(posterImageView)
            .addDefaultRequestListener(object : RequestListener<Any> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Any>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Any?,
                    model: Any?,
                    target: Target<Any>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

            })
            .load(movie.posterUrl)
            .into(posterImageView)
    }

    private fun FragmentMovieDetailsBinding.bindFavorite(favoriteAvailability: FavoriteAvailability) {
        when (favoriteAvailability) {
            FavoriteAvailability.Unavailable -> {
                favoriteImageView.setOnClickListener(null)
                favoriteImageView.gone()
            }
            is FavoriteAvailability.Available -> {
                favoriteImageView.setFavoriteImageRes(favoriteAvailability)
                favoriteImageView.setOnClickListener {
                    viewModel.updateFavorite(movie)
                }
            }
        }
    }

    private fun ImageView.setFavoriteImageRes(favoriteAvailability: FavoriteAvailability.Available) {
        val imageRes = favoriteAvailability.drawableResId()
        setImageResource(imageRes)
        visible()
    }

    private fun FragmentMovieDetailsBinding.bindReleaseDate() =
        when (val date = movie.releaseDate) {
            ReleaseDate.Unavailable -> dateTextView.gone()
            is ReleaseDate.Available -> {
                dateTextView.text = date.value
                dateTextView.visible()
            }
        }

    private fun FragmentMovieDetailsBinding.bindRatingBar() {
        when (val movieRating = movie.rating) {
            MovieRating.Empty -> ratingBar.gone()
            is MovieRating.Available -> {
                ratingBar.rating = movieRating.value
                ratingBar.visible()
            }
        }
    }

    private fun registerObservers() {
        with(viewModel) {
            observeDetails()
            observeFavoriteStatusChanged()
            observeGenericError()
            observeNetworkError()
        }
    }

    private fun MovieDetailsViewModel.observeDetails() =
        details.observe(viewLifecycleOwner, Observer {
            EspressoIdlingResource.isIdle()
            if (it == null) {
                showEmptyDetailsUi()
            } else {
                bindMovieDetailsUi(it)
            }
        })

    private fun showEmptyDetailsUi() {
        with(binding) {
            loading.gone()
            hideDetailsViews()
            retryGroup.visible()
        }
    }

    private fun FragmentMovieDetailsBinding.hideDetailsViews() {
        genresTextView.gone()
        overviewGroup.gone()
        directorGroup.gone()
        castGroup.gone()
        similarMoviesGroup.gone()
        reviewsGroup.gone()
    }

    private fun bindMovieDetailsUi(details: MovieDetails) {
        with(binding) {
            loading.gone()

            bindBasicDetailsUi(details)

            bindDirectorUi(details)

            bindMovieReviewsUi(details)

            bindSimilarMoviesUi(details)
        }
    }

    private fun FragmentMovieDetailsBinding.bindBasicDetailsUi(details: MovieDetails) {
        genresTextView.text = details.getGenres()
        overviewTextView.text = details.overview
        castTextView.text = details.getCast()

        genresTextView.visible()
        overviewGroup.visible()
        castGroup.visible()
    }

    private fun FragmentMovieDetailsBinding.bindDirectorUi(details: MovieDetails) =
        when (val director = details.director) {
            Director.Empty -> directorGroup.gone()
            is Director.Available -> {
                directorTextView.text = director.name
                directorGroup.visible()
            }
        }

    private fun FragmentMovieDetailsBinding.bindMovieReviewsUi(details: MovieDetails) {
        when (val movieReviews = details.reviews) {
            MovieReviews.Unavailable -> reviewsGroup.gone()
            is MovieReviews.Available -> {
                val reviews = movieReviews.reviews
                if (reviews.isEmpty()) {
                    reviewsGroup.gone()
                    return
                }
                bindNonEmptyReviewsUi(reviews)
            }
        }
    }

    private fun FragmentMovieDetailsBinding.bindNonEmptyReviewsUi(reviews: List<MovieReview>) {
        val firstReview = reviews.first()
        review1.bindSingleReviewLayout(firstReview)
        reviewsCaptionTextView.visible()

        val hasSecondReview = reviews.size == 2
        if (hasSecondReview) {
            review2.bindSingleReviewLayout(reviews[1])
        } else {
            review2.layout.gone()
        }
    }

    private fun IncludeReviewLayoutBinding.bindSingleReviewLayout(movieReview: MovieReview) {
        authorTextView.text = movieReview.author
        contentTextView.text = movieReview.content
        layout.visible()
    }

    private fun FragmentMovieDetailsBinding.bindSimilarMoviesUi(details: MovieDetails) {
        when (val similarMovies = details.similarMovies) {
            SimilarMovies.Unavailable -> similarMoviesGroup.gone()
            is SimilarMovies.Available -> {
                similarMoviesGroup.visible()
                adapter.setPosters(similarMovies.posters)
            }
        }
    }

    private fun MovieDetailsViewModel.observeFavoriteStatusChanged() {
        favoriteStatusUpdated.observe(viewLifecycleOwner, Observer {
            binding.bindFavorite(it)
        })
    }

    private fun MovieDetailsViewModel.observeGenericError() =
        genericError.observe(viewLifecycleOwner, Observer {
            val message = getString(R.string.generic_error, it)
            updateUiOnError(message)
        })

    private fun MovieDetailsViewModel.observeNetworkError() =
        networkError.observe(viewLifecycleOwner, Observer {
            if (it) {
                val message = getString(R.string.no_network_connection)
                updateUiOnError(message)
            }
        })

    private fun updateUiOnError(errorMessage: String) {
        showEmptyDetailsUi()
        showToast(errorMessage)
        EspressoIdlingResource.isIdle()
    }

    private fun showToast(message: String) {
        ToastManager.showToast(requireContext(), message)
    }

    private fun fetchDetails() {
        setupUiForFetching()
        viewModel.fetchDetails(movie)
        EspressoIdlingResource.isNotIdle()
    }

    private fun setupUiForFetching() {
        with(binding) {
            loading.visible()
            hideDetailsViews()
            retryGroup.gone()
        }
    }

    companion object {
        private const val MOVIE_PARAM = "movie_param"
        private const val TRANSITION_NAME_PARAM = "transition_name_param"

        @JvmStatic
        fun newInstance(movie: Movie, transitionName: String) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MOVIE_PARAM, movie)
                    putString(TRANSITION_NAME_PARAM, transitionName)
                }
            }
    }
}
