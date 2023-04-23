package com.developer.aitek.api

import com.developer.aitek.api.data.*

class Repository(
    private val remoteRequestManager: RemoteRequestManager
): SafeApiRequest() {

    suspend fun lists(page: Int = 0, query: String = "", location: String = "", is_full_time: Boolean = false): MutableList<ItemJob> {
        return apiRequest { remoteRequestManager.getLists(page, query, location, is_full_time) }
    }

    suspend fun detail(id: String): ItemJob {
        return apiRequest { remoteRequestManager.getDetail(id) }
    }
}