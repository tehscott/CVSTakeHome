package com.stromberg.cvstakehome.network

import com.stromberg.cvstakehome.model.FlickrResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {
    @GET("photos_public.gne?format=json&nojsoncallback=1")
    fun getImages(
        @Query("tags") tags: String
    ): Call<FlickrResponse>

    companion object {
        const val BASE_URL = "https://api.flickr.com/services/feeds/"
    }
}