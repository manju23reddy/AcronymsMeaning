package com.manju23reddy.acronymsmeaning.ui.screens

import androidx.lifecycle.ViewModel
import com.manju23reddy.acronymsmeaning.repo.AcronymRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repo: AcronymRepo
) : ViewModel() {


}