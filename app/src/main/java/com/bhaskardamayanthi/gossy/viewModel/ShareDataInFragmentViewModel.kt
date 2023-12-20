package com.bhaskardamayanthi.gossy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareDataInFragmentViewModel:ViewModel() {
    private val _dataUpdated = MutableLiveData<Boolean>()
    val dataUpdated: LiveData<Boolean> get() = _dataUpdated

    val sharedData = MutableLiveData<String>()
    val getParentPostId = MutableLiveData<String>()
    val getParentTokenId = MutableLiveData<String>()
    val parentPhoneNumber = MutableLiveData<String>()

    fun setData(data: String, postId: String, tokenId: String, phoneNumber: String) {
        // Set your data here
        sharedData.value = data
        getParentPostId.value = postId
        getParentTokenId.value = tokenId
        parentPhoneNumber.value = phoneNumber

        // Trigger the event when data is updated
        _dataUpdated.value = true
    }
}