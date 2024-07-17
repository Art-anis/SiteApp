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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImage
import com.nerazim.siteapp.R
import com.nerazim.siteapp.ScaffoldState
import com.nerazim.siteapp.ui.theme.SiteAppTheme

@Composable
fun AddSiteScreen(
    scaffoldState: MutableState<ScaffoldState>,
    goToBrowseScreen: () -> Unit,
    goBack: () -> Unit
) {
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
        //siteDetails = viewModel.siteUiState.siteDetails,
        //onValueChange = viewModel::updateUiState
    )
}

@Preview(showBackground = true)
@Composable
fun AddSiteScreenPreview() {
    SiteAppTheme {
        val state = remember {
            mutableStateOf(ScaffoldState(
                {},
                {},
                {}
            ))
        }
        AddSiteScreen(scaffoldState = state, goToBrowseScreen = { /*TODO*/ }) {

        }
    }
}