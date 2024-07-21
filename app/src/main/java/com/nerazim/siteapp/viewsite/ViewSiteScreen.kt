package com.nerazim.siteapp.viewsite

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nerazim.siteapp.AppViewModelProvider
import com.nerazim.siteapp.R
import com.nerazim.siteapp.ScaffoldState
import com.nerazim.siteapp.addsite.SiteDetails
import com.nerazim.siteapp.addsite.SiteUiState
import com.nerazim.siteapp.browse.Site
import com.nerazim.siteapp.nav.NavigationDestination
import com.nerazim.siteapp.ui.theme.SiteAppTheme

@Composable
fun ViewSiteScreen(
    scaffoldState: MutableState<ScaffoldState>,
    goToAddScreen: () -> Unit,
    goToEditScreen: (Int) -> Unit,
    goBack: () -> Unit,
    viewModel: SiteDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()

    scaffoldState.value = ScaffoldState(
        title = {
            Text(stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
                    .merge(TextStyle(
                        fontWeight = FontWeight.Bold
                    ))
            )
        },
        topBarActions = {
            IconButton(onClick = {
                goToAddScreen()
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_site)
                )
            }
            IconButton(onClick = {
                goToEditScreen(uiState.value.siteDetails.id)
            }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null
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


    
    SiteData(uiState.value)
}

@Composable
fun SiteData(
    site: SiteDetailsUiState
) {
    val context = LocalContext.current
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
            text = site.siteDetails.name,
            style = MaterialTheme.typography.titleLarge
                .merge(TextStyle(fontWeight = FontWeight.Bold))
        )
        Spacer(modifier = Modifier.height(12.dp))
        AsyncImage(
            model = site.siteDetails.image,
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
            text = site.siteDetails.description,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = site.siteDetails.link,
            style = MaterialTheme.typography.bodyLarge
                .merge(
                    TextStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ),
            modifier = Modifier.clickable {
                try {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(site.siteDetails.link)))
                }
                catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "URL does not exist! Please fix the link.", Toast.LENGTH_LONG).show()
                }
            }
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
        ViewSiteScreen(scaffoldState = state, goToAddScreen = {}, goToEditScreen = {  }, goBack = {})
    }
}
