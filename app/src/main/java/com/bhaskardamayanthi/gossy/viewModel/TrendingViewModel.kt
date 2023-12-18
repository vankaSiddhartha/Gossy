package com.bhaskardamayanthi.gossy.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhaskardamayanthi.gossy.model.PostModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TrendingViewModel : ViewModel() {
    private val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("post")
    private val reference = database.orderByChild("likes").limitToLast(10)


    val dataList = MutableLiveData<List<PostModel>>()
    val isLoading = MutableLiveData<Boolean>()

    init {
        fetchDataFromDatabase()
    }

    private fun fetchDataFromDatabase() {
        isLoading.value = true

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = mutableListOf<PostModel>()
                for (childSnapshot in snapshot.children.reversed()) {
                    val yourData = childSnapshot.getValue(PostModel::class.java)
                    yourData?.let { data.add(it) }
                }
                dataList.value = data
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
