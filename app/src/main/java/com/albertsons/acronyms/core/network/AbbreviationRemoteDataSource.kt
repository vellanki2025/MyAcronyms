package com.albertsons.acronyms.core.network

import javax.inject.Inject

class AbbreviationRemoteDataSource @Inject constructor(private val api: AbbreviationApiInterface) {

    /**
     * API to get Abbreviations
     */
    suspend fun getAbbreviation(query: String) = api.getAbbreviation(query)
}