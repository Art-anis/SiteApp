package com.nerazim.siteapp.addsite

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nerazim.siteapp.AppViewModelProvider
import com.nerazim.siteapp.R
import com.nerazim.siteapp.ScaffoldState
import com.nerazim.siteapp.edit.SiteForm
import com.nerazim.siteapp.ui.theme.SiteAppTheme
import kotlinx.coroutines.launch

@Composable
fun AddSiteScreen(
    scaffoldState: MutableState<ScaffoldState>,
    goToBrowseScreen: () -> Unit,
    goBack: () -> Unit,
    viewModel: SiteEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
        siteDetails = viewModel.siteUiState.siteDetails,
        onValueChange = viewModel::updateUiState
    )
}

//Preview теперь не работает из-за viewModel
//@Preview(showBackground = true)
//@Composable
//fun AddSiteScreenPreview() {
//    SiteAppTheme {
//        val state = remember {
//            mutableStateOf(ScaffoldState(
//                {},
//                {},
//                {}
//            ))
//        }
//        AddSiteScreen(scaffoldState = state, goToBrowseScreen = { /*TODO*/ }, goBack = {})
//    }
//}