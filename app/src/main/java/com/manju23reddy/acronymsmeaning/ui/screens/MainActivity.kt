package com.manju23reddy.acronymsmeaning.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
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
                            SearchBoxUI()
                            Spacer(modifier = Modifier.height(5.dp))
                            DataResults()
                        }

                 )
                }
            }
        }
    }
    
    @Composable
    fun ReqTypeBox(modifier: Modifier = Modifier){
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
    fun SearchBoxUI(){
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth(1f)
            .height(60.dp)) {

            val (type, searchtxt, searchBtn) = createRefs()

            ReqTypeBox(modifier = Modifier.constrainAs(type){
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })


            var textVal by remember {
                mutableStateOf("")
            }
            TextField(value = textVal, onValueChange = {
                textVal = it
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

            IconButton(onClick = { }, modifier = Modifier
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
    fun DataResults(){

    }
}

