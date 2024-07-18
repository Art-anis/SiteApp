package com.nerazim.siteapp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nerazim.siteapp.AppViewModelProvider
import com.nerazim.siteapp.R
import com.nerazim.siteapp.ScaffoldState
import com.nerazim.siteapp.browse.BrowseViewModel
import com.nerazim.siteapp.ui.theme.SiteAppTheme

@Composable
fun HomeScreen(
    scaffoldState: MutableState<ScaffoldState>,
    goToAddScreen: () -> Unit,
    goToViewScreen: (Int) -> Unit,
    goToBrowseScreen: () -> Unit
) {
    scaffoldState.value = ScaffoldState(
        title = {
            Text(text = stringResource(id = R.string.app_name))
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
            IconButton(onClick = goToBrowseScreen) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.browse_sites)
                )
            }
        },
        bottomBar = {

        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.city),
            contentDescription = null,
            modifier = Modifier.height(80.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.placeholder),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(384.dp)
                .clickable(onClick = {
                    goToViewScreen(0)
                })
        )

        Text(
            text = stringResource(id = R.string.main_page_question),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = Color(0xFFD0BCFF),
                    shape = RoundedCornerShape(10.dp)
                )
                .width(200.dp)
        )

        Button(
            onClick = { /*TODO generate button*/ },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.generate),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            text = stringResource(id = R.string.about_title),
            style = MaterialTheme.typography.titleLarge
                .merge(TextStyle(fontWeight = FontWeight.Bold)),
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.about_description),
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SiteAppTheme {
        val state = remember {
            mutableStateOf(ScaffoldState())
        }
        HomeScreen(
            scaffoldState = state,
            goToAddScreen = { /*TODO*/ },
            goToViewScreen = { /*TODO*/ }) {

        }
    }
}