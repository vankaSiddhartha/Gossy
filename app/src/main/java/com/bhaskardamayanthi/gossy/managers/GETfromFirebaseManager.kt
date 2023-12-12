package com.bhaskardamayanthi.gossy.managers

import com.bhaskardamayanthi.gossy.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GETfromFirebaseManager {
    fun getUserDataAndSaveInLocalData(uid:String){
        val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        DATABASE.child("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}