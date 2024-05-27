package com.stromberg.cvstakehome.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.stromberg.cvstakehome.helper.FlickrHelper
import com.stromberg.cvstakehome.model.FlickrItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val flickrHelper: FlickrHelper,
) : ViewModel() {
    private var lastSearch: Job? = null

    var searchText = mutableStateOf("")
        private set

    var isSearching = mutableStateOf(false)
        private set

    var searchResults = mutableStateOf<List<FlickrItem>>(listOf())
        private set

    fun onQueryChange(text: String) {
        searchText.value = text
    }

    fun onSearch(text: String) = CoroutineScope(Dispatchers.IO).launch {
        searchText.value = text

        lastSearch?.cancel() // cancel previous search if it isn't already done
        lastSearch = launch {
            delay(SEARCH_DELAY)
            isSearching.value = true
            searchResults.value = flickrHelper.search(text).orEmpty()
            isSearching.value = false
        }
    }

    fun onActiveChange(active: Boolean) {}

    companion object {
        private const val SEARCH_DELAY = 250L
    }
}