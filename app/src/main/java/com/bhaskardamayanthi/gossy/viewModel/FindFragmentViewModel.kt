package com.bhaskardamayanthi.gossy.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhaskardamayanthi.gossy.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FindFragmentViewModel: ViewModel() {
    private val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users")
    val dataList = MutableLiveData<MutableList<UserModel>>()
    val isLoding = MutableLiveData<Boolean>()

init {
    getUserFromDatabase()
}
    fun getUserFromDatabase(){
        isLoding.value = true
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val yourData = mutableListOf<UserModel>()
                for (childSnapshot in snapshot.children){
                    val dataPack = childSnapshot.getValue(UserModel::class.java)
                    if (dataPack != null) {
                        yourData.add(dataPack)
                    }
                }
                dataList.value = yourData
                isLoding.value = false
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}