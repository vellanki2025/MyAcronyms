package com.albertsons.acronyms.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Interface for providing coroutines dispatchers for different contexts.
 */
interface DispatcherProvider {

    /**
     * The [CoroutineDispatcher] for the main/UI thread.
     */
    val main: CoroutineDispatcher

    /**
     * The [CoroutineDispatcher] for the IO thread, typically used for disk or network IO operations.
     */
    val io: CoroutineDispatcher

    /**
     * The [CoroutineDispatcher] for the default thread, typically used for CPU-intensive operations.
     */
    val default: CoroutineDispatcher
}

/**
 * Default implementation of [DispatcherProvider].
 * This class provides coroutines dispatchers for different contexts using the [Dispatchers] object.
 */
class DefaultDispatcherProvider @Inject constructor() : DispatcherProvider {

    /**
     * The [CoroutineDispatcher] for the main/UI thread provided by [Dispatchers.Main].
     */
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main

    /**
     * The [CoroutineDispatcher] for the IO thread provided by [Dispatchers.IO].
     */
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    /**
     * The [CoroutineDispatcher] for the default thread provided by [Dispatchers.Default].
     */
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
}
