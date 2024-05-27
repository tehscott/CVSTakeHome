package com.stromberg.cvstakehome.network

import com.stromberg.cvstakehome.helper.FlickrHelper
import com.stromberg.cvstakehome.model.FlickrItem
import javax.inject.Inject

class FlickrHelperImpl @Inject constructor(
    private val flickrService: FlickrService
) : FlickrHelper {
    override suspend fun search(tags: String): List<FlickrItem>? {
        var flickrItems: List<FlickrItem>? = null

        flickrService.getImages(tags).execute().let { response ->
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    runCatching {
                        flickrItems = responseBody.items
                    }
                }
            }
        }

        return flickrItems
    }
}