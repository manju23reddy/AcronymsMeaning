package com.manju23reddy.acronymsmeaning.repo

import com.manju23reddy.acronymsmeaning.model.AcronymResult
import com.manju23reddy.acronymsmeaning.model.REQParamsType
import com.manju23reddy.acronymsmeaning.net.AcronymServiceImpl
import com.manju23reddy.acronymsmeaning.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class AcronymRepo @Inject constructor(val netHelper: AcronymServiceImpl,
                                      val scope : CoroutineDispatcher = Dispatchers.IO ){

    private val result = MutableSharedFlow<Result<List<AcronymResult>>>()

    suspend fun getSearchResultFor(type : REQParamsType, query : String) :
            MutableSharedFlow<Result<List<AcronymResult>>> {
        withContext(scope){
            netHelper.getAcronymResult(type, query).collect{res ->
                result.emit(res)
            }
        }
        return result
    }



}