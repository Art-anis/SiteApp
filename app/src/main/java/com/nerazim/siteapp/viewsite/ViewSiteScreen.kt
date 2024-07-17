package com.nerazim.siteapp.viewsite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nerazim.siteapp.R
import com.nerazim.siteapp.ScaffoldState
import com.nerazim.siteapp.browse.Site
import com.nerazim.siteapp.nav.NavigationDestination
import com.nerazim.siteapp.ui.theme.SiteAppTheme

object DetailsDestination: NavigationDestination {
    override val route = "site_details"
    const val itemId = "itemId"
    val routeWithArgs = "${route}/{$itemId}"
}

@Composable
fun ViewSiteScreen(
    scaffoldState: MutableState<ScaffoldState>,
    goToAddScreen: (Int) -> Unit,
    site: Site,
    goBack: () -> Unit
) {
    scaffoldState.value = ScaffoldState(
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        topBarActions = {
            IconButton(onClick = {
                goToAddScreen(0)
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_site)
                )
            }
        },
        bottomBar = {
            BottomAppBar {
                Button(onClick = goBack) {
                    Text(stringResource(id = R.string.back_btn))
                }
            }
        }
    )
    
    SiteData(site)
}

@Composable
fun SiteData(
    site: Site
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.city),
            contentDescription = null,
            modifier = Modifier.height(80.dp)
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = site.name,
            style = MaterialTheme.typography.titleLarge
                .merge(TextStyle(fontWeight = FontWeight.Bold))
        )
        Spacer(modifier = Modifier.height(12.dp))
        //TODO fix after changing image source to URI
        AsyncImage(
            model = site.image,
            contentDescription = null,
            placeholder = rememberVectorPainter(image = Icons.Default.Place),
            error = rememberVectorPainter(image = Icons.Default.Place),
            fallback = rememberVectorPainter(image = Icons.Default.Place),
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = site.description,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = site.link,
            style = MaterialTheme.typography.bodyLarge
                .merge(
                    TextStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ViewSiteScreenPreview() {
    SiteAppTheme {
        val state = remember {
            mutableStateOf(ScaffoldState())
        }
        ViewSiteScreen(scaffoldState = state, site = Site("Кремль"), goToAddScreen = {}, goBack = { /*TODO*/ })
    }
}