package com.bhaskardamayanthi.gossy.managers

import android.content.Context
import android.content.Intent
import com.bhaskardamayanthi.gossy.auth.PermissionActivity
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.model.NotificationModel
import com.bhaskardamayanthi.gossy.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GETfromFirebaseManager {
    fun getUserDataAndSaveInLocalData(uid:String,context: Context){
        val storeManager = StoreManager(context)

        val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        DATABASE.child("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                storeManager.saveString("name",userData?.name.toString())

                 storeManager.saveString("sex",userData?.gender.toString())
                 storeManager.saveString("fakeName",userData?.fakeName.toString())
                storeManager.saveString("fakeImg",userData?.fakeImg.toString())
                storeManager.saveString("dobYear",userData?.dob.toString())
              storeManager.saveString("college",userData?.collegeName.toString())
                storeManager.saveString("number",userData?.phone.toString())
                context.startActivity(Intent(context, PermissionActivity::class.java))


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    fun getLikeCount(context:Context,number:String,id: String, type:String,callback: (Long,Boolean) -> Unit) {
        val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        val likesRef = DATABASE.child("likes").child(id)
        val college = StoreManager(context).getString("college","Bro")
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
                        DATABASE.child("post").child(college).child(id).child("likes").setValue(count)
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



    fun seen(userId: String, notificationId:String){
        val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
      DATABASE.child("notifications").child(userId).child(notificationId).child("seen").setValue(true).addOnSuccessListener {

        }
    }

    fun getNotificationCount(userId: String,callback: (Long) -> Unit){
      var count =0L
        Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("notifications").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (childSnap in snapshot.children){
                        if (childSnap.exists()){
                            val getData = childSnap.getValue(NotificationModel::class.java)
                            if (getData != null) {
                                if (!getData.seen){
                                    count += 1
                                }

                            }
                            callback(count)
                        }
                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }



    }