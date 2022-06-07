package com.manju23reddy.acronymsmeaning.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manju23reddy.acronymsmeaning.model.AcronymResult
import com.manju23reddy.acronymsmeaning.model.REQParamsType
import com.manju23reddy.acronymsmeaning.repo.AcronymRepo
import com.manju23reddy.acronymsmeaning.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface MainActivityUiState{


    data class NoResult(
        val error : String? = null
    ) : MainActivityUiState

    data class HasResult(
        val results : List<AcronymResult>? = null
    ) : MainActivityUiState

    data class isLoading(val value : Boolean? = null):MainActivityUiState
}

private data class ViewModelState(
    var result: List<AcronymResult>? = null,
    var isLoading: Boolean = false,
    var searchType: REQParamsType = REQParamsType.SF,
    var queryString: String = "",
    var error: String? = null
){
    fun toMainActivityUIState() : MainActivityUiState =
        if (isLoading){
            MainActivityUiState.isLoading(true)
        }
        else {
            if (result == null) {
                MainActivityUiState.NoResult(error = error)
            } else {
                MainActivityUiState.HasResult(results = result)
            }
        }
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repo: AcronymRepo
) : ViewModel() {

    private val searchResult = MutableStateFlow(ViewModelState())

    val uiState = searchResult.map {
        it.toMainActivityUIState()
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        searchResult.value.toMainActivityUIState()
    )
    init {
        searchResult.update {
            it.copy(
                result = null,
                isLoading = false,
                error = null
            )
        }
    }

    fun getResultForSearch(type : REQParamsType, query : String){
        viewModelScope.launch {

            repo.getSearchResultFor(type, query).collect{result ->
                when(result){
                    is Result.Success -> {
                        searchResult.update {
                            it.copy(result = result.data, isLoading = false )
                        }
                    }
                    is Result.Error -> {
                        searchResult.update {
                            it.copy(error = result.error.toString() , isLoading = false)
                        }
                    }
                    is Result.Loading -> {
                        searchResult.update {
                            it.copy(isLoading = true, result = null, error = null )
                        }
                    }
                }
            }

        }
    }


}