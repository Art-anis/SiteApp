package com.nerazim.siteapp.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.nerazim.siteapp.nav.NavigationDestination
import com.nerazim.siteapp.ui.theme.SiteAppTheme
import com.nerazim.siteapp.viewsite.SiteDetailsViewModel
import kotlinx.coroutines.launch

@Composable
fun EditSiteScreen(
    scaffoldState: MutableState<ScaffoldState>,
    goToBrowseScreen: () -> Unit,
    goBack: () -> Unit,
    viewModel: SiteEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    scaffoldState.value = ScaffoldState(
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        topBarActions = {
            IconButton(onClick = goToBrowseScreen) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.browse_sites)
                )
            }
        },
        bottomBar = {
            BottomAppBar {
                Row {
                    Button(onClick = {
                        coroutineScope.launch {
                            viewModel.saveSite()
                            goBack()
                        }
                    }) {
                        Text(text = stringResource(id = R.string.save_btn))
                    }
                    Button(onClick = goBack) {
                        Text(text = stringResource(id = R.string.back_btn))
                    }
                }
            }
        }
    )

    SiteForm(
        siteUiState = viewModel.siteUiState,
        onValueChange = viewModel::updateUiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteForm(
    siteUiState: SiteUiState,
    onValueChange: (SiteDetails) -> Unit
) {

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            onValueChange(siteUiState.siteDetails.copy(image = it ?: Uri.EMPTY))
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
            stringResource(id = R.string.add_site_title),
            style = MaterialTheme.typography.titleLarge
                .merge(TextStyle(fontWeight = FontWeight.Bold))
        )
        Spacer(modifier = Modifier.height(6.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = siteUiState.siteDetails.image,
                contentDescription = null,
                placeholder = rememberVectorPainter(image = Icons.Default.Place),
                error = rememberVectorPainter(image = Icons.Default.Place),
                fallback = rememberVectorPainter(image = Icons.Default.Place),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.upload_image_text),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alpha(0.5f)
            )
        }
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = siteUiState.siteDetails.name,
            onValueChange = {
                onValueChange(siteUiState.siteDetails.copy(name = it))
            },
            placeholder = {
                Text(
                    stringResource(id = R.string.name_field_placeholder),
                    modifier = Modifier.alpha(0.5f)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = siteUiState.siteDetails.description,
            onValueChange = {
                onValueChange(siteUiState.siteDetails.copy(description = it))
            },
            placeholder = {
                Text(
                    stringResource(id = R.string.description_field_placeholder),
                    modifier = Modifier.alpha(0.5f)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .height(150.dp)
                .verticalScroll(rememberScrollState())
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = siteUiState.siteDetails.link,
            onValueChange = {
                onValueChange(siteUiState.siteDetails.copy(link = it))
            },
            placeholder = {
                Text(
                    "Add link to website...",
                    modifier = Modifier.alpha(0.5f)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun EditSiteScreenPreview() {
    SiteAppTheme {
        val state = remember {
            mutableStateOf(ScaffoldState(
                {},
                {},
                {}
            ))
        }
        EditSiteScreen(scaffoldState = state, goToBrowseScreen = { }, goBack = {})    }
}