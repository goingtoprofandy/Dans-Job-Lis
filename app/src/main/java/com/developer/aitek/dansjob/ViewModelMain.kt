package com.developer.aitek.dansjob

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.developer.aitek.api.ApiException
import com.developer.aitek.api.ConnectionException
import com.developer.aitek.api.Repository
import com.developer.aitek.api.data.*

class ViewModelMain(
    private val repository: Repository
): ViewModel() {

    val dataRes = MutableLiveData<MutableList<ItemJob>>()
    val dataDetailRes = MutableLiveData<ItemJob>()
    val isLoading = MutableLiveData<Boolean>().apply { value = false }

    fun loadData(page:Int = 0, query: String = "", location: String = "", is_full_time: Boolean = false, onError: (String) -> Unit) {
        isLoading.postValue(true)
        Coroutines.main {
            try {
                val response = repository.lists(page, query, location, is_full_time)
                dataRes.postValue(response)
            } catch (e: ApiException) {
                onError(e.message.toString())
            } catch (e: ConnectionException) {
                onError(e.message.toString())
            }
            isLoading.postValue(false)
        }
    }

    fun detailData(id:String, deviceID: String, onError: (String) -> Unit, onSuccess: () -> Unit) {
        isLoading.postValue(true)
        Coroutines.main {
            try {
                val response = repository.detail(id)
                dataDetailRes.postValue(response)
                onSuccess()
            } catch (e: ApiException) {
                onError(e.message.toString())
            } catch (e: ConnectionException) {
                onError(e.message.toString())
            }
            isLoading.postValue(false)
        }
    }

}