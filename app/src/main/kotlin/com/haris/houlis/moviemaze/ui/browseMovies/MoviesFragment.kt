package com.haris.houlis.moviemaze.ui.browseMovies

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.haris.houlis.moviemaze.R
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.databinding.FragmentMoviesBinding
import com.haris.houlis.moviemaze.extensions.*
import com.haris.houlis.moviemaze.ui.browseMovies.MoviesViewModel.SearchMovieQuery.*
import com.haris.houlis.moviemaze.ui.handler.LoadStateErrorHandler
import com.haris.houlis.moviemaze.ui.manager.ToastManager
import com.haris.houlis.moviemaze.ui.moviesDetails.MovieDetailsFragment
import com.haris.houlis.moviemaze.util.EspressoIdlingResource
import kotlinx.android.synthetic.main.fragment_movies.view.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MoviesFragment : ScopeFragment(), MoviesAdapter.MovieClickListener {

    private lateinit var coroutineScope: LifecycleCoroutineScope

    private val viewModel: MoviesViewModel by viewModel { parametersOf(requireActivity()) }
    private val loadStateHandler: LoadStateErrorHandler by inject()

    private val adapter = MoviesAdapter(this)
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EspressoIdlingResource.isNotIdle()

        coroutineScope = viewLifecycleOwner.lifecycleScope

        drawBehindStatusBar()

        initAdapter()

        setupUi()

        registerObservers()
    }

    private fun drawBehindStatusBar() {
        requireActivity().statusBarColor(R.color.colorAccent)

        with(binding) {
            root.fitToSystemWindows(root)
        }
    }

    private fun initAdapter() =
        with(adapter) {
            addStateListener()
            scrollToTopWhenNewResultsArrive()
        }

    private fun MoviesAdapter.addStateListener() =
        addLoadStateListener { loadState ->
            updateUiOnNewLoadSate(loadState)
            showMessageIfLoadErrorOccurred(loadState)
        }

    private fun updateUiOnNewLoadSate(loadState: CombinedLoadStates) {
        val refreshLoadState = loadState.source.refresh
        binding.progressBar
            .isVisible = refreshLoadState is LoadState.Loading
    }

    private fun showMessageIfLoadErrorOccurred(loadState: CombinedLoadStates) {
        val throwable = loadStateHandler.getLoadStateErrorThrowable(loadState)
        throwable?.let {
            ToastManager.showToast(requireContext(), it.message)
        }
    }

    private fun MoviesAdapter.scrollToTopWhenNewResultsArrive() {
        coroutineScope.launch {
            loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.moviesRecyclerView.scrollToPosition(0) }
        }
    }

    private fun setupUi() {
        with(binding) {
            setupRecyclerView()
            setupInputLayout()
            setupSwipeToRefresh()
        }
    }

    private fun FragmentMoviesBinding.setupRecyclerView() {
        with(moviesRecyclerView) {
            itemAnimator = null
            layoutManager = getListLayoutManager()
            adapter = this@MoviesFragment.adapter
        }
    }

    private fun getListLayoutManager(): GridLayoutManager {
        val spanCount = if (isPortrait()) 1 else 2
        return GridLayoutManager(context, spanCount)
    }

    private fun FragmentMoviesBinding.setupInputLayout() {
        inputEditText.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == IME_ACTION_SEARCH) {
                    hideImeAndClearFocus()
                    updateShowListFromInput()
                }
                false
            }

            doOnTextChanged { text, _, before, _ ->
                val previousInputWasNotEmptyAndCurrentOneIs = before != 0 && text.isNullOrEmpty()
                if (previousInputWasNotEmptyAndCurrentOneIs) {
                    hideImeAndClearFocus()
                    viewModel.getPopularMovies()
                }
            }
        }
    }

    private fun updateShowListFromInput() {
        val input = userInput()
        if (!input.isNullOrBlank()) {
            viewModel.searchMovies(input)
        }
    }

    private fun userInput(): String? = binding.inputEditText.text?.toString()

    private fun FragmentMoviesBinding.setupSwipeToRefresh() =
        swipeToRefresh.setOnRefreshListener {
            adapter.refresh()
        }

    private fun registerObservers() =
        with(viewModel) {
            observeMovies()
            observeQuery()
        }

    private fun MoviesViewModel.observeMovies() {
        coroutineScope.launchWhenStarted {
            pagingDataFlow.collectLatest {
                EspressoIdlingResource.isIdle()
                binding.swipeToRefresh.isRefreshing = false
                adapter.submitData(it)
            }
        }
    }

    private fun MoviesViewModel.observeQuery() {
        coroutineScope.launch {
            queryFlow.collectLatest {
                val message = when (it) {
                    Initial -> ""
                    is Valid -> it.value
                }
                binding.inputEditText.setText(message)
            }
        }
    }

    override fun onMovieClick(movie: Movie, posterImage: ImageView) {
        val transitionName = posterImage.transitionName
        val destinationFragment = MovieDetailsFragment.newInstance(movie, transitionName)
        parentFragmentManager.beginTransaction()
            .addSharedElement(posterImage, transitionName)
            .replace(R.id.main_activity_root, destinationFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onFavoriteClick(movie: Movie) {
        viewModel.updateFavorite(movie)
    }
}
