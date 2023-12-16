package com.bhaskardamayanthi.gossy.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareDataInFragmentViewModel:ViewModel() {
    val sharedData = MutableLiveData<String>()
    val getParentPostId =MutableLiveData<String>()
    val getParentTokenId = MutableLiveData<String>()
}