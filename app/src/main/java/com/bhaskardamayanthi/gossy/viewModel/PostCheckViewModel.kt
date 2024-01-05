package com.bhaskardamayanthi.gossy.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhaskardamayanthi.gossy.model.ButuluModel
import com.bhaskardamayanthi.gossy.model.PostModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostCheckViewModel():ViewModel( ) {
    private val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("butulu")
    private val reference = database.orderByChild("time")

    val dataList = MutableLiveData<ArrayList<String>>()
    val isLoading = MutableLiveData<Boolean>()

    init {
        fetchDataFromDatabase()

    }
    private fun fetchDataFromDatabase() {
        isLoading.value = true

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              val list = ArrayList<String>()
                for (childSnapshot in snapshot.children) {
                    val getData = childSnapshot.getValue(ButuluModel::class.java)
                    if (getData != null) {
                        list.add(getData.bhutu.toString())
                    }

                }
                dataList.value = list

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}