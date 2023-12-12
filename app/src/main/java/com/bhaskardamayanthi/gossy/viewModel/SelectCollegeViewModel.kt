package com.bhaskardamayanthi.gossy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhaskardamayanthi.gossy.model.CollageModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectCollegeViewModel:ViewModel() {
    val databaseReference = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("college")
    private val _collageData: MutableLiveData<List<CollageModel>> = MutableLiveData()
    val collageData: LiveData<List<CollageModel>> = _collageData
    private val _loadingStatus: MutableLiveData<Boolean> = MutableLiveData()
    val loadingStatus: LiveData<Boolean> = _loadingStatus

    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    fun fetchData() {
        _loadingStatus.value = true // Show progress bar
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val collages = mutableListOf<CollageModel>()
                        for (snapshot in dataSnapshot.children) {
                            val collage = snapshot.getValue(CollageModel::class.java)
                            collage?.let {
                                collages.add(collage)
                            }
                        }
                        _collageData.postValue(collages)

                        _loadingStatus.value = false // Hide progress bar
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle any errors that occur during data retrieval
                        _loadingStatus.value = false // Hide progress bar
                    }
                })
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}