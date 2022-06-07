package com.manju23reddy.acronymsmeaning.model

import com.google.gson.annotations.SerializedName

data class VariationObject(
    @SerializedName("lf")
    val lf : String,
    @SerializedName("freq")
    val freq : Int,
    @SerializedName("since")
    val since : Int
)

data class AcronymResult(
    @SerializedName("sf")
    val sf : String? = null,
    @SerializedName("lfs")
    val lfs : ArrayList<LongFormObject> = ArrayList()
)

data class LongFormObject(
    @SerializedName("lf")
     val lf : String? = null,
    @SerializedName("freq")
    val freq : Int = 0,
    @SerializedName("since")
    val since : Int = 0,
    @SerializedName("vars")
    var vars: ArrayList<VariationObject> = ArrayList()
)

enum class REQParamsType(type : String){
    SF("sf"),
    LF("lf");
}

enum class RESPONSE_TYPE(type : String){
    SF("sf"),
    LFS("lfs");
}


