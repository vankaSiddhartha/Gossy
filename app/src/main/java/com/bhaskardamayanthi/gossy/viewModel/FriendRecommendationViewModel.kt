package com.bhaskardamayanthi.gossy.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhaskardamayanthi.gossy.model.Contact
import com.bhaskardamayanthi.gossy.model.UserModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FriendRecommendationViewModel : ViewModel() {

    private val _friendRecommendations = MutableLiveData<List<UserModel>>()
    val friendRecommendations: LiveData<List<UserModel>> get() = _friendRecommendations
    fun fetch(userContacts: List<Contact>){
        fetchFriendRecommendations(userContacts)
    }

    fun fetchFriendRecommendations(userContacts: List<Contact>) {
        val databaseReference = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users")

        val matchedUsers = mutableListOf<UserModel>()
         _friendRecommendations.value = emptyList()
        for (contact in userContacts) {
            databaseReference.orderByChild("phone").equalTo(contact.phoneNumber)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (userSnapshot in snapshot.children) {
                            val userId = userSnapshot.key
                            val user = userSnapshot.getValue(UserModel::class.java)
                            user?.let { matchedUsers.add(it) }
                        }
                        _friendRecommendations.value = matchedUsers
                     //  Log.e("data bro",matchedUsers.toString())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle onCancelled event if needed
                    }
                })
        }
    }
}
