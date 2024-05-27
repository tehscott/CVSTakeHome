package com.stromberg.cvstakehome.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.stromberg.cvstakehome.R
import com.stromberg.cvstakehome.model.FlickrItem
import com.stromberg.cvstakehome.model.Media
import com.stromberg.cvstakehome.nav.Destination
import com.stromberg.cvstakehome.ui.theme.CVSTakeHomeTheme
import com.stromberg.cvstakehome.viewmodel.DetailsViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsView(
    navController: NavController,
    flickrItem: FlickrItem,
) {
    val viewModel = DetailsViewModel(flickrItem)

    val photo = remember { viewModel.flickrItem }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                ),
                title = {
                    Text(
                        text = photo.title,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    if (Destination.Details.showBack) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                            )
                        }
                    }
                },
            )
        }
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Bottom,
            ) {
                flickrItem.let { photo ->
                    val imageSize = remember { mutableStateOf(Pair(0, 0)) }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photo.media.url)
                                .crossfade(true)
                                .placeholder(R.drawable.placeholder)
                                .size(Size.ORIGINAL)
                                .build(),
                            //placeholder = painterResource(id = R.drawable.placeholder),
                            contentDescription = photo.title,
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.Center,
                            filterQuality = FilterQuality.High,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            onSuccess = {
                                val w = it.result.drawable.toBitmap().width
                                val h = it.result.drawable.toBitmap().height
                                imageSize.value = Pair(w, h)
                            }
                        )
                    }
                    val published = OffsetDateTime.parse(photo.published, DateTimeFormatter.ISO_DATE_TIME)
                        .format(DateTimeFormatter.ofPattern("MM/dd/yy"))

                    // Getting the actual image size is proving difficult. I'm not sure why the bitmap isn't giving the proper size.
                    Text(
                        text = "Published on $published, ${imageSize.value.first}x${imageSize.value.second}",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(96.dp)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Text(
                            text = photo.tags,
                            style = TextStyle(
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.None
                                )
                            ),
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    CVSTakeHomeTheme {
        DetailsView(rememberNavController(), FlickrItem("", "", Media(""), "", ""))
    }
}