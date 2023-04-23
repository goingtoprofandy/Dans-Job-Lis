package com.developer.aitek.api.data

data class CustomResponseMeta<T>(
    var meta: T
): BaseResponse()