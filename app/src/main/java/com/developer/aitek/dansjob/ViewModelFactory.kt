package com.developer.aitek.dansjob

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developer.aitek.api.Repository

class ViewModelFactoryMain(
    private val repository: Repository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModelMain(
            repository
        ) as T
    }
}