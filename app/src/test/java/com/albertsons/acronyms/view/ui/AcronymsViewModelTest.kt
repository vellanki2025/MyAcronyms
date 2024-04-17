package com.albertsons.acronyms.view.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.albertsons.acronyms.core.DefaultDispatcherProvider
import com.albertsons.acronyms.core.domain.AcronymState
import com.albertsons.acronyms.model.AbbreviationData
import com.albertsons.acronyms.core.network.NetworkState
import com.albertsons.acronyms.core.repository.AbbreviationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class AcronymsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: AbbreviationViewModel

    @Mock
    private lateinit var defaultDispatcherProvider: DefaultDispatcherProvider
    @Mock
    private lateinit var mockRepo: AbbreviationRepository
    @Mock
    private lateinit var mockObserver: Observer<AcronymState>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = AbbreviationViewModel(mockRepo, defaultDispatcherProvider)
        viewModel.abbreviationState.observeForever(mockObserver)

        `when`(defaultDispatcherProvider.io).thenReturn(testDispatcher)
    }

    @After
    fun tearDown() {
        viewModel.abbreviationState.removeObserver(mockObserver)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test successful response`() = runBlocking {
        val mockData = AbbreviationData()
        val lfs = AbbreviationData.AbbreviationDataItem.Lf()
        mockData.add(AbbreviationData.AbbreviationDataItem(lfs = arrayListOf(lfs)))
        val mockResponse = NetworkState.Success(mockData)
        `when`(mockRepo.getMeaningsData(anyString())).thenReturn(mockResponse)

        viewModel.performSearch("test")

        verify(mockObserver).onChanged(AcronymState.Loading)
        assert(viewModel.abbreviationState.value is AcronymState.Success)
    }

    @Test
    fun `test failure response`() = runBlocking {
        val mockData = AbbreviationData()
        mockData.add(AbbreviationData.AbbreviationDataItem(lfs = arrayListOf()))
        val mockResponse = NetworkState.Success(mockData)
        `when`(mockRepo.getMeaningsData(anyString())).thenReturn(mockResponse)

        viewModel.performSearch("test")

        verify(mockObserver).onChanged(AcronymState.Loading)
        assert(viewModel.abbreviationState.value is AcronymState.Failure)
    }

}
