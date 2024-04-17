package com.albertsons.acronyms.core.domain

import com.albertsons.acronyms.model.AcronymWords

/**
 * AcronymState for Success / Failure / Empty / Loading
 */
sealed interface AcronymState {

    data class Success(val data: List<AcronymWords>): AcronymState

    data class Failure(val errorMessage: String?): AcronymState

    data class Empty(val displayMessage: String?): AcronymState

    data object Loading: AcronymState
}