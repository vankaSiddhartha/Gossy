package com.bhaskardamayanthi.gossy.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhaskardamayanthi.gossy.adapter.TagFriendAdapter
import com.bhaskardamayanthi.gossy.model.NotificationModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NotificationViewModel: ViewModel() {
    private val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("notifications")
    val liveData = MutableLiveData<MutableList<NotificationModel>>()
    val isLoading = MutableLiveData<Boolean>()

    fun fetchData(userId:String){
getNotificationData(userId)
    }
    private fun getNotificationData(userId:String){
        isLoading.value = true
       database.child(userId).orderByChild("time").addValueEventListener(object :ValueEventListener{
           val data = mutableListOf<NotificationModel>()
           override fun onDataChange(snapshot: DataSnapshot) {
               for (childSnapshot in snapshot.children){
                   val getData = childSnapshot.getValue(NotificationModel::class.java)
                   if (getData != null) {
                       data.add(getData)
                   }
               }
               liveData.value= data
               isLoading.value =false
           }

           override fun onCancelled(error: DatabaseError) {

           }

       })
    }

}