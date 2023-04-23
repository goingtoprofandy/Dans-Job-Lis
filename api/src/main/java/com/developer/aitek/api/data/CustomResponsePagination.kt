package com.developer.aitek.api.data

data class CustomResponsePagination<T>(
    var data: T,
    var meta: Meta
): BaseResponse() {
    data class Meta(
        var page: Int,
        var next_page: Int,
        var total_fetch: Int,
        var limit: Int,
        var total_page: Int,
    )
}