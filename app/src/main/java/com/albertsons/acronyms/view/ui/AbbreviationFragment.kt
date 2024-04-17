package com.albertsons.acronyms.view.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.albertsons.acronyms.R
import com.albertsons.acronyms.databinding.AbbreviationsLayoutBinding
import com.albertsons.acronyms.core.domain.AcronymState
import com.albertsons.acronyms.utils.DataParser
import com.albertsons.acronyms.view.adapter.WordsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AbbreviationFragment : BaseFragment<AbbreviationsLayoutBinding>(R.layout.abbreviations_layout) {

    // The view model associated with this fragment.
    private val viewModel: AbbreviationViewModel by activityViewModels()

    @Inject
    lateinit var wordsListAdapter: WordsListAdapter

    override fun setUpFragmentUI(view: View?) {
        setUpRecyclerView()
        configureObservers()
        configureListeners()
    }

    /**
     * Sets up the RecyclerView to display the list of words.
     */
    private fun setUpRecyclerView() {
        binding.recyclerview.adapter = wordsListAdapter

        // create a vertical layout manager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.addItemDecoration(
            DividerItemDecoration(
                context,
                layoutManager.orientation
            )
        )
    }

    /**
     * Configure Observers
     */
    private fun configureObservers() {
        lifecycleScope.launch {
            viewModel.abbreviationState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    // Display a loading state when the data is being fetched.
                    is AcronymState.Loading -> {
                        // clear list
                        wordsListAdapter.setList(emptyList())
                        binding.textInvalid.isVisible = false
                        binding.progressDialog.isVisible = true
                    }

                    // Display the list of words when data is successfully retrieved.
                    is AcronymState.Success -> {
                        binding.progressDialog.isVisible = false
                        binding.textInvalid.isVisible = false
                        wordsListAdapter.setList(state.data)
                        binding.recyclerview.isVisible = true
                    }

                    // Display an error message when there is a failure while fetching data.
                    is AcronymState.Failure -> {
                        binding.progressDialog.isVisible = false
                        binding.recyclerview.isVisible = false
                        binding.textInvalid.apply {
                            isVisible = true
                            text = state.errorMessage
                        }
                    }

                    // Display a message when there is no data to show.
                    is AcronymState.Empty -> {
                        binding.textInvalid.apply {
                            isVisible = true
                            text = state.displayMessage
                        }
                        //binding.recyclerview.isVisible = false
                    }
                }
            }
        }
    }

    /**
     * Configure Listeners
     */
    private fun configureListeners() {
        binding.wordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
            }
            true
        }

        binding.searchBtn.setOnClickListener {
            performSearch()
        }

        binding.resetBtn.setOnClickListener {
            binding.wordEditText.text?.clear()
            // clear list
            wordsListAdapter.setList(emptyList())
        }
    }

    /**
     * Hide keyboard and perform search
     */
    private fun performSearch() {
        binding.wordEditText.hideKeyboard()
        val abbreviation = binding.wordEditText.text.toString()
        val isValidAbbreviation = DataParser.isValidShorForm(abbreviation)

        when {
            !isValidAbbreviation.first -> {
                Toast.makeText(requireContext(), isValidAbbreviation.second, Toast.LENGTH_LONG)
                    .show()
            }

            else -> {
                viewModel.performSearch(abbreviation)
                binding.recyclerview.smoothScrollToPosition(0)
            }
        }
    }

    /**
     * Hide Keyboard
     */
    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}
