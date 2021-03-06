package com.manju23reddy.acronymsmeaning.net

import android.util.Log
import com.manju23reddy.acronymsmeaning.model.AcronymResult
import com.manju23reddy.acronymsmeaning.model.REQParamsType
import com.manju23reddy.acronymsmeaning.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface AcronymService{
    @GET("dictionary.py?")
    fun getAcronymResultSF(@Query("sf") req : String) : Call<List<AcronymResult>>
    @GET("dictionary.py?")
    fun getAcronymResultLS(@Query("lf")req : String): Call<List<AcronymResult>>



}

@Singleton
class AcronymServiceImpl @Inject constructor()  {

    private val BASE_URL = "http://www.nactem.ac.uk/software/acromine/"

    private var service : AcronymService? = null


    init {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(
            BASE_URL).build()

        service = retrofit.create(AcronymService::class.java)

    }

    public val result = MutableSharedFlow<Result<List<AcronymResult>>>()

    suspend fun getAcronymResult(type : REQParamsType, query : String):
            MutableSharedFlow<Result<List<AcronymResult>>>{

        withContext(Dispatchers.IO){
            result.emit(Result.Loading)

            val callType = when(type){
                REQParamsType.SF -> {
                    service?.getAcronymResultSF(query)
                }
                REQParamsType.LF -> {
                    service?.getAcronymResultLS(query)
                }
            }

            callType?.enqueue(object : Callback<List<AcronymResult>>{
                override fun onResponse(
                    call: Call<List<AcronymResult>>,
                    response: Response<List<AcronymResult>>
                ) {
                    response?.let {
                        it.body()?.let {
                            CoroutineScope(Dispatchers.IO).launch {
                                Log.e("test", it.toString())
                                result.emit(Result.Success(it))
                            }
                        }
                    } ?: CoroutineScope(Dispatchers.IO).launch {
                        result.emit(Result.Error(Error("NO Response")))
                    }
                }

                override fun onFailure(call: Call<List<AcronymResult>>, t: Throwable) {
                    CoroutineScope(Dispatchers.IO).launch {
                        result.emit(Result.Error(Error("NO Response")))
                    }
                }

            })
        }


        return result
    }



}

