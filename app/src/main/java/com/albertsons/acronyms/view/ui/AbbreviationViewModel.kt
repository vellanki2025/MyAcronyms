package com.albertsons.acronyms.view.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertsons.acronyms.core.DefaultDispatcherProvider
import com.albertsons.acronyms.core.domain.AcronymState
import com.albertsons.acronyms.model.AcronymWords
import com.albertsons.acronyms.model.AbbreviationData
import com.albertsons.acronyms.core.network.NetworkState
import com.albertsons.acronyms.core.repository.AbbreviationRepository
import com.albertsons.acronyms.utils.ErrorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class AbbreviationViewModel @Inject constructor(
    private val repo: AbbreviationRepository,
    private val dispatcherProvider: DefaultDispatcherProvider
) : ViewModel() {

    private val _acronymState = MutableLiveData<AcronymState>()

    /**
     * LiveData to observe the current state of the acronym words data.
     */
    val abbreviationState: LiveData<AcronymState> get() = _acronymState

    /**
     * API call to fetch data for acronym provided by user
     */
    fun performSearch(sortForm: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            _acronymState.postValue(AcronymState.Loading)
            try {
                when (val response = repo.getMeaningsData(sortForm)) {
                    is NetworkState.Success -> {
                        getWordsFromList(response.data)
                    }
                    is NetworkState.Error -> {
                        _acronymState.postValue(AcronymState.Failure(response.toString()))
                    }
                }
            } catch (ex: UnknownHostException) {
                _acronymState.postValue(AcronymState.Failure(ErrorUtils.NETWORK_ERROR_MESSAGE))
            } catch (ex: java.lang.Exception) {
                _acronymState.postValue(AcronymState.Failure(ErrorUtils.INVALID_RESPONSE))
            }
        }
    }

    /**
     * Get words from response
     */
    @VisibleForTesting
    private fun getWordsFromList(acronymData: AbbreviationData) {
        if ((acronymData.isNotEmpty()) && (acronymData[0].lfs.isNotEmpty())) {
            val tempLfArrayList = mutableListOf<String>()
            for (lfItem in acronymData[0].lfs) {
                tempLfArrayList.add(lfItem.lf)
            }
            val acronymWordsList: List<AcronymWords> = tempLfArrayList.map { AcronymWords(it) }
            _acronymState.postValue(AcronymState.Success(acronymWordsList))
        } else {
            _acronymState.postValue(AcronymState.Failure(ErrorUtils.RESPONSE_ERROR_MESSAGE))
        }
    }
}