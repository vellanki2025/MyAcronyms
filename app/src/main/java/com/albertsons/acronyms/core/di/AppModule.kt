package com.albertsons.acronyms.core.di

import android.content.Context
import com.albertsons.acronyms.core.network.AbbreviationApiInterface
import com.albertsons.acronyms.core.network.Constants
import com.albertsons.acronyms.core.network.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideStoresApi(@ApplicationContext appContext: Context): AbbreviationApiInterface {
        // Create an HTTP logging interceptor for logging network requests and responses.
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }

        // Create an OkHttpClient with the HTTP logging interceptor and other configurations.
        val client = OkHttpClient().newBuilder()
            .addInterceptor(NetworkConnectionInterceptor(appContext))
            .addInterceptor(interceptor)
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()

        // Build the Retrofit instance with the base URL, OkHttpClient, and Gson converter factory.
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AbbreviationApiInterface::class.java)
    }
}