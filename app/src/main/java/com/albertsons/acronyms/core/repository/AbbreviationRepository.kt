package com.albertsons.acronyms.core.repository

import com.albertsons.acronyms.model.AbbreviationData
import com.albertsons.acronyms.core.network.AbbreviationRemoteDataSource
import com.albertsons.acronyms.core.network.NetworkState
import javax.inject.Inject

class AbbreviationRepository @Inject constructor(
    private val dataSource: AbbreviationRemoteDataSource
) {
    /**
     * Get Meanings data from the network
     */
    suspend fun getMeaningsData(sortForm: String): NetworkState<AbbreviationData> {
        val response = dataSource.getAbbreviation(sortForm)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }
}