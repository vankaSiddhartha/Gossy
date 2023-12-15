package com.bhaskardamayanthi.gossy.managers

import android.content.Context
import android.widget.Toast
import com.bhaskardamayanthi.gossy.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GETfromFirebaseManager {
    fun getUserDataAndSaveInLocalData(uid:String,context: Context){
        val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        DATABASE.child("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                Toast.makeText(context, userData.toString(), Toast.LENGTH_SHORT).show()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    fun getLikeCount(id: String, callback: (Long) -> Unit) {
        val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        val likesRef = DATABASE.child("likes").child(id)

        likesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount
                callback(count) // Pass the count to the callback function
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if any
            }
        })
    }


}