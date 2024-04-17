package com.albertsons.acronyms.core.network

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * An OkHttp interceptor that checks for network connectivity before making requests.
 *
 * @property mContext The [Context] used to check for network connectivity.
 * @constructor Creates an instance of [NetworkConnectionInterceptor].
 */
class NetworkConnectionInterceptor(private val mContext: Context) : Interceptor {
    /**
     * Intercepts the request chain to ensure network connectivity.
     *
     * @param chain The [Interceptor.Chain] for the request.
     * @return The response from the chain.
     * @throws NoConnectivityException if there is no internet connectivity.
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected) {
            throw NoConnectivityException()
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    /**
     * Checks if the device is connected to the network.
     *
     * @return `true` if connected to the network, `false` otherwise.
     */
    private val isConnected: Boolean
        get() {
            val connectivityManager =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }
}

/**
 * Custom exception class to represent a lack of internet connectivity.
 */
class NoConnectivityException : IOException() {
    /**
     * Returns the message associated with this exception.
     *
     * @return The exception message.
     */
    override val message: String
        get() = "No Internet Connection"
}
