package com.stromberg.cvstakehome.di

import com.stromberg.cvstakehome.helper.FlickrHelper
import com.stromberg.cvstakehome.network.FlickrHelperImpl
import com.stromberg.cvstakehome.network.FlickrService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FlickrHelperModule {
    @Binds
    abstract fun bindFlickrHelper(
        flickrHelperImpl: FlickrHelperImpl
    ): FlickrHelper
}

@Module
@InstallIn(SingletonComponent::class)
object FlickrServiceModule {
    @Provides
    @Singleton
    fun provideFlickrService(): FlickrService =
        Retrofit.Builder()
            .baseUrl(FlickrService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlickrService::class.java)
}