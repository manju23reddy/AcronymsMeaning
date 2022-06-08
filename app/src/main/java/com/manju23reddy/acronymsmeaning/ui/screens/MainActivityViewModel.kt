package com.manju23reddy.acronymsmeaning.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manju23reddy.acronymsmeaning.model.AcronymResult
import com.manju23reddy.acronymsmeaning.model.REQParamsType
import com.manju23reddy.acronymsmeaning.repo.AcronymRepo
import com.manju23reddy.acronymsmeaning.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
 data class ViewModelState(
    var result: List<AcronymResult>? = null,
    var isLoading: Boolean = false,
    var searchType: REQParamsType = REQParamsType.SF,
    var queryString: String = "hmm",
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

    val searchResult = MutableStateFlow(ViewModelState())

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

    fun getResultForSearch(){
        viewModelScope.launch {
            _getResult()
        }
    }

    private suspend fun _getResult(){
        withContext(Dispatchers.IO){
            repo.getSearchResultFor(searchResult.value.searchType, searchResult.value.queryString).collectLatest { result ->
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

    fun setReqType(type: REQParamsType){
        _update(type, searchResult.value.queryString)
    }

    fun getQueryString() : String{
        return searchResult.value.queryString
    }

    fun setQeryString(query : String){
        _update(searchResult.value.searchType, query)
    }

    private fun _update(type: REQParamsType, query: String){
        searchResult.update {
            it.copy(
                searchType = type,
                queryString = it.queryString,
                result = it.result,
                isLoading = it.isLoading,
                error = it.error
            )
        }
    }


}