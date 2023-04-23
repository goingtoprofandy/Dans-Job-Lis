package com.developer.aitek.api.data

data class CustomResponseDetail<K, V>(
    var data: K,
    var meta: V
): BaseResponse()