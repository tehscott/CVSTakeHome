package com.stromberg.cvstakehome.helper

import com.stromberg.cvstakehome.model.FlickrItem

interface FlickrHelper {
    suspend fun search(tags: String): List<FlickrItem>?
}