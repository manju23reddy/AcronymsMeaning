package com.manju23reddy.acronymsmeaning.model

data class VariationObject(
    val lf : String,
    val freq : Int,
    val since : Int
)

data class LongFormObject(
    val lf : String,
    val freq : Int,
    val since : Int,
    val vars : List<VariationObject>
)

enum class REQParamsType(type : String){
    SF("sf"),
    LF("lf");
}

enum class RESPONSE_TYPE(type : String){
    SF("sf"),
    LFS("lfs");
}


