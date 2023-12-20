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
    fun getLikeCount(number:String,id: String, type:String,callback: (Long,Boolean) -> Unit) {
        val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        val likesRef = DATABASE.child("likes").child(id)

        likesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount
                // Pass the count to the callback function
                if (snapshot.hasChild(number)){
                    callback(count,true)
                }else{
                    callback(count,false)
                }
                if (type.equals("post")) {
                    if (id.isNotEmpty())
                    DATABASE.child("post").child(id).child("likes").setValue(count)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if any
            }
        })
    }
fun getCommentsCount(postId:String,callback: (Long) -> Unit){
    val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
    val usersRef = DATABASE.child("comments").child(postId)
    usersRef.addValueEventListener(object :ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            callback(snapshot.childrenCount)

        }

        override fun onCancelled(error: DatabaseError) {
        }

    })
}
    fun checkIfFriend(userId:String,friendId:String,callback: (Boolean) -> Unit){
        val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("friends").child(userId)
        DATABASE.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(friendId)){
                    callback(true)
                }else{
                    callback(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    fun seen(userId: String,notificationId:String){
        val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("notifications").child(userId).child(notificationId).child("seen").setValue(true)
    }


}