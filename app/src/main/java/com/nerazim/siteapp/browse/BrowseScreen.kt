package com.nerazim.siteapp.browse

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nerazim.siteapp.AppViewModelProvider
import com.nerazim.siteapp.R
import com.nerazim.siteapp.ScaffoldState
import com.nerazim.siteapp.addsite.SiteDetails
import com.nerazim.siteapp.addsite.toSiteDetails
import com.nerazim.siteapp.db.SiteEntity

data class Site(
    val name: String,
    val description: String = "",
    val image: Uri = Uri.EMPTY,
    val link: String = ""
)

@Composable
fun BrowseScreen(
    scaffoldState: MutableState<ScaffoldState>,
    goToAddScreen: () -> Unit,
    goToEditScreen: (Int) -> Unit,
    goToViewScreen: (Int) -> Unit,
    viewModel: BrowseViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    scaffoldState.value = ScaffoldState(
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        topBarActions = {
            IconButton(onClick = {
                goToAddScreen()
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_site)
                )
            }
        },
        bottomBar = {

        }
    )

    val browseUiState by viewModel.browseUiState.collectAsState()

    val sites: List<Site> = listOf(Site("Кремль"), Site("Петропавловская крепость"))


    LazyColumn {
        items(browseUiState.siteList) { site ->
            SiteItem(
                site = site.toSiteDetails(),
                goToEditScreen = goToEditScreen,
                goToViewScreen = goToViewScreen
            )
        }
    }
}

@Composable
fun SiteItem(
    site: SiteDetails,
    goToEditScreen: (Int) -> Unit,
    goToViewScreen: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable {
                goToViewScreen(site.id)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(
                    start = 10.dp,
                    end = 10.dp
                )
        ) {
            AsyncImage(
                model = site.image,
                contentDescription = null,
                placeholder = rememberVectorPainter(image = Icons.Default.Place),
                error = rememberVectorPainter(image = Icons.Default.Place),
                fallback = rememberVectorPainter(image = Icons.Default.Place),
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = site.name,
                style = MaterialTheme.typography.titleLarge
                    .merge(
                        TextStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .height(30.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = site.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(50.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    goToViewScreen(site.id)
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
                IconButton(onClick = {
                    goToEditScreen(site.id)
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                }
                IconButton(onClick = { /*TODO delete*/ }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}