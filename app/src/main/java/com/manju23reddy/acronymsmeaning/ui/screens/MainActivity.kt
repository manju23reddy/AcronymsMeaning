package com.manju23reddy.acronymsmeaning.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.manju23reddy.acronymsmeaning.R
import com.manju23reddy.acronymsmeaning.model.REQParamsType
import com.manju23reddy.acronymsmeaning.ui.theme.AcronymsMeaningTheme
import com.manju23reddy.acronymsmeaning.ui.theme.iconColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

    val reqType = mutableListOf<String>(REQParamsType.SF.name, REQParamsType.LF.name)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AcronymsMeaningTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {(

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()) {
                            SearchBoxUI(mainActivityViewModel)
                            Spacer(modifier = Modifier.height(5.dp))
                            DataResults(mainActivityViewModel)
                        }

                 )
                }
            }
        }
    }
    
    @Composable
    fun ReqTypeBox(modifier: Modifier = Modifier, state : MainActivityViewModel){
        var curSelection : String by remember {
            mutableStateOf(reqType[0])
        }
        var expanded : Boolean by remember {
            mutableStateOf(false)
        }
        Box(modifier = modifier.fillMaxWidth(0.2f), contentAlignment = Alignment.Center){
            Row( modifier = Modifier
                .padding(5.dp)
                .clickable {
                    expanded = !expanded
                },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = curSelection,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(end = 5.dp)
                )
                
                Icon(imageVector = Icons.Filled.ArrowDropDown , contentDescription = "drop")
                
                DropdownMenu(expanded = expanded, onDismissRequest = {
                    expanded = false
                }, modifier = Modifier.wrapContentSize()) {
                    reqType.forEach { type->
                        DropdownMenuItem(onClick = {
                            curSelection = type
                            state.setReqType(REQParamsType.valueOf(type))
                            expanded = false
                        }) {
                            Text(text = type)
                        }
                    }

                }
            }
        }
    }

    @Composable
    fun SearchBoxUI(mainActivityViewModel: MainActivityViewModel) {

        val uiState by mainActivityViewModel.uiState.collectAsState()

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth(1f)
            .height(60.dp)) {

            val (type, searchtxt, searchBtn) = createRefs()

            ReqTypeBox(modifier = Modifier.constrainAs(type){
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }, mainActivityViewModel)


            var textVal by remember {
                mutableStateOf(mainActivityViewModel.searchResult.value.queryString)
            }
            TextField(value = textVal, onValueChange = {
                textVal = it
                mainActivityViewModel.setQeryString(textVal)
            }, modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(60.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topEnd = 5.dp, topStart = 5.dp)
                )
                .constrainAs(searchtxt) {
                    start.linkTo(type.end, margin = 5.dp)
                    top.linkTo(parent.top, margin = 5.dp)
                }, placeholder = { Text(text = "Search Acromine")})

            IconButton(onClick = {
              mainActivityViewModel.getResultForSearch()
            }, modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(0.15f)
                .constrainAs(searchBtn) {
                    start.linkTo(searchtxt.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                Icon(painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = "search_icon", tint = iconColor )
            }

        }
    }

    @Composable
    fun DataResults(mainActivityViewModel: MainActivityViewModel) {
        val state by mainActivityViewModel.uiState.collectAsState()
        Column(modifier = Modifier
            .wrapContentSize()
            .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            when(state){
                is MainActivityUiState.NoResult -> {
                    Text(text = "No Result",
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(1f),
                        textAlign = TextAlign.Center)
                }
                is MainActivityUiState.HasResult -> {
                    val response = (state as MainActivityUiState.HasResult).results
                    LazyColumn{
                        response?.let {items->
                            item {
                                Text(text = items[0].sf!!,
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .fillMaxWidth(1f),
                                    textAlign = TextAlign.Center)
                            }



                            val inner = items[1].lfs
                            inner.let {innerItem ->
                                innerItem.forEach {cur->
                                    item {
                                        cur.lf?.let { it1 ->

                                            Column(modifier = Modifier.wrapContentSize()) {
                                                Text(text = it1,
                                                    modifier = Modifier
                                                        .padding(20.dp)
                                                        .fillMaxWidth(1f),
                                                    textAlign = TextAlign.Center)


                                            }

                                        }

                                    }

                                    cur.vars.let {vars->
                                      vars.forEach {varsItem->
                                          item {
                                              Column(modifier = Modifier.wrapContentSize()) {

                                                  Text(text = varsItem.lf,
                                                      modifier = Modifier
                                                          .padding(20.dp)
                                                          .fillMaxWidth(1f),
                                                      textAlign = TextAlign.Center)

                                                  Text(text = varsItem.freq.toString(),
                                                      modifier = Modifier
                                                          .padding(20.dp)
                                                          .fillMaxWidth(1f),
                                                      textAlign = TextAlign.Center)

                                                  Text(text = varsItem.since.toString(),
                                                      modifier = Modifier
                                                          .padding(20.dp)
                                                          .fillMaxWidth(1f),
                                                      textAlign = TextAlign.Center)
                                              }


                                          }
                                      }
                                    }

                                }

                            }
                        }

                    }



                }
                is MainActivityUiState.isLoading -> {
                    CircularProgressIndicator(progress = 0.70f)
                }
            }

        }
    }
}

