package com.stromberg.cvstakehome.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.stromberg.cvstakehome.R
import com.stromberg.cvstakehome.nav.Destination
import com.stromberg.cvstakehome.ui.theme.CVSTakeHomeTheme
import com.stromberg.cvstakehome.viewmodel.SearchViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun SearchView(navController: NavController) {
    val viewModel: SearchViewModel = hiltViewModel()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val searchResults = remember { viewModel.searchResults }
    val listState = rememberLazyListState()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Black,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                ),
                title = {
                    SearchBar(
                        query = viewModel.searchText.value,
                        onQueryChange = viewModel::onSearch,
                        onSearch = {
                            viewModel.onSearch(it)
                            keyboardController?.hide()
                        },
                        active = false,
                        onActiveChange = viewModel::onActiveChange,
                        placeholder = { Text("Search Flickr") },
                        leadingIcon = { Icon(Icons.Outlined.Search, "Search") },
                        trailingIcon = {
                            if (viewModel.searchText.value.isNotEmpty()) {
                                IconButton(onClick = {
                                    viewModel.onQueryChange("")
                                    keyboardController?.show()
                                }) {
                                    Icon(Icons.Outlined.Clear, "Clear")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp, end = 16.dp),
                    ) {}
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(4.dp)
        ) {
            val pullRefreshState =
                rememberPullRefreshState(viewModel.isSearching.value, { viewModel.onSearch(viewModel.searchText.value) })

            Box {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .pullRefresh(pullRefreshState),
                ) {
                    items(searchResults.value) { item ->
                        AsyncImage(
                            //model = item.media.url,
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.media.url)
                                .crossfade(true)
                                .placeholder(R.drawable.placeholder)
                                .size(Size.ORIGINAL)
                                .build(),
                            placeholder = painterResource(id = R.drawable.placeholder),
                            contentDescription = item.title,
                            contentScale = ContentScale.FillWidth,
                            filterQuality = FilterQuality.High,
                            modifier = Modifier
                                .clickable(onClick = {
                                    val destination = navController.graph.findNode(Destination.Details.route)
                                    if (destination != null) {
                                        navController.navigate(
                                            destination.id,
                                            bundleOf("flickrItem" to item)
                                        )
                                    }
                                })
                                .padding(4.dp)
                                .fillMaxWidth()
                        )
                    }
                }

                // I like the look of this over a normal circular progress bar
                PullRefreshIndicator(viewModel.isSearching.value, pullRefreshState, Modifier.align(Alignment.TopCenter))
            }

            // Empty state
            if (viewModel.searchResults.value.isEmpty()
                && !viewModel.isSearching.value) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Empty state - search"
                        )
                        Text(
                            text = "There's nothing here.",
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    CVSTakeHomeTheme {
        SearchView(rememberNavController())
    }
}