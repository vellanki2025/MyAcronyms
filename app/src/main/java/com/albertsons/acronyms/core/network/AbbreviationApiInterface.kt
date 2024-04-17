package com.albertsons.acronyms.core.network

import com.albertsons.acronyms.model.AbbreviationData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AbbreviationApiInterface {

    @GET(Constants.GET_DICTIONARY)
    suspend fun getAbbreviation(@Query(Constants.SEARCH_QUERY) query: String): Response<AbbreviationData>
}