package com.albertsons.acronyms.repository

import com.albertsons.acronyms.model.AbbreviationData
import com.albertsons.acronyms.core.network.AbbreviationRemoteDataSource
import com.albertsons.acronyms.core.network.NetworkState
import com.albertsons.acronyms.core.repository.AbbreviationRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class AbbreviationRepositoryTest {

    private lateinit var acronymRepository: AbbreviationRepository

    @Mock
    lateinit var abbreviationRemoteDataSource: AbbreviationRemoteDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        acronymRepository = AbbreviationRepository(abbreviationRemoteDataSource)
    }

    @Test
    fun `test successful response`() = runBlocking {
        val acronymData = AbbreviationData()
        // Mock successful response
        Mockito.`when`(abbreviationRemoteDataSource.getAbbreviation(anyString())).thenReturn(Response.success(acronymData))

        // Call the function
        val response = acronymRepository.getMeaningsData("test")

        // Assert the response
        assertNotNull(response)
        assertTrue(response is NetworkState.Success)
        assertEquals(acronymData, (response as NetworkState.Success).data)
    }

    @Test
    fun `test failure response`() = runBlocking {
        // Mock a ResponseBody
        val mockBody = mock(ResponseBody::class.java)

        // Create a Response using Response.error
        val errorCode = 500
        val errorResponse = Response.error<AbbreviationData>(errorCode, mockBody)

        // Mock failure response
        Mockito.`when`(abbreviationRemoteDataSource.getAbbreviation(anyString()))
            .thenReturn(errorResponse)

        // Call the function
        val response = acronymRepository.getMeaningsData("test")

        // Assert the response
        assertNotNull(response)
        assertTrue(response is NetworkState.Error)
    }
}