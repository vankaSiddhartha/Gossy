package com.bhaskardamayanthi.gossy.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhaskardamayanthi.gossy.model.PostModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebasePagingViewModel : ViewModel() {


    val dataList = MutableLiveData<List<PostModel>>()
    val isLoading = MutableLiveData<Boolean>()




    private var lastKey: String? = null
    private val PAGE_SIZE = 10 // Number of items to fetch per page

    fun load(college: String,context: Context){
        loadNextPage(college,context) // Initial data load
    }

    fun loadNextPage(college: String, context: Context) {
        val databaseReference =
            Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("post")
                .child(college)
                .orderByChild("time")

        if (isLoading.value == true) return // To avoid multiple simultaneous requests

        isLoading.value = true

        val query = if (lastKey == null) {
            databaseReference.limitToFirst(PAGE_SIZE)
        } else {
            databaseReference.startAfter(lastKey).limitToFirst(PAGE_SIZE)
        }

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = mutableListOf<PostModel>()
                for (childSnapshot in snapshot.children) {
                    val yourData = childSnapshot.getValue(PostModel::class.java)
                    yourData?.let { data.add(it) }
                }

                if (data.isNotEmpty()) {
                    val newDataList = dataList.value.orEmpty().toMutableList()
                    newDataList.addAll(data)
                    dataList.value = newDataList
                    lastKey = data.last().time // Update lastKey only if new data is fetched
                } else {
                    // If no new data is fetched, you might want to handle this case
                    // For example, notify the user or log a message
                    Toast.makeText(context, "loading stop", Toast.LENGTH_SHORT).show()
                }

                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
                isLoading.value = false
            }
        })
    }
}
