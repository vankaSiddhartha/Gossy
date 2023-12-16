package com.bhaskardamayanthi.gossy.managers

import android.content.Context
import android.content.Intent
import android.util.Log
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bhaskardamayanthi.gossy.MainActivity
import com.bhaskardamayanthi.gossy.auth.PermissionActivity
import com.bhaskardamayanthi.gossy.loading.Loading.dismissDialogForLoading
import com.bhaskardamayanthi.gossy.loading.Loading.showAlertDialogForLoading
import com.bhaskardamayanthi.gossy.model.PostModel
import com.bhaskardamayanthi.gossy.model.UserModel
import com.bhaskardamayanthi.gossy.notifications.NotificationData
import com.bhaskardamayanthi.gossy.notifications.PushNotifications
import com.bhaskardamayanthi.gossy.notifications.RetrofitInstance
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FirebaseDataManager {

    val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference


    fun uploadUserToDatabase(userId: String, user: UserModel, context: Context) {
        showAlertDialogForLoading(context)
        // Assuming you have a "users" node in your database
        val usersRef = DATABASE.child("users")

        // Set the user object under the user's ID node
        usersRef.child(userId).setValue(user)
            .addOnSuccessListener {
                dismissDialogForLoading()
//                SweetAlertDialog(
//                    context,
//                    SweetAlertDialog.SUCCESS_TYPE
//                ).setTitleText("Good job!").setContentText("successful").setConfirmClickListener { sDialog -> // Showing simple toast message to user
//                    sDialog.dismissWithAnimation()
                    context.startActivity(Intent(context, PermissionActivity::class.java))
//                }.setConfirmClickListener { sDialog ->
//                    sDialog.dismissWithAnimation()
//                  //  context.startActivity(Intent(context, LoginActivity::class.java))
//
//                }.show()
            }
            .addOnFailureListener {
                dismissDialogForLoading()
                // Handle failed upload
                SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
                    .setContentText(it.toString()).show()
            }
    }

    fun uploadPostToDatabase(userId: String, post: PostModel, context: Context) {
        showAlertDialogForLoading(context)
        // Assuming you have a "users" node in your database
        val usersRef = DATABASE.child("post")

        // Set the user object under the user's ID node
        usersRef.child(post.id!!).setValue(post)
            .addOnSuccessListener {
                dismissDialogForLoading()
                SweetAlertDialog(
                    context,
                    SweetAlertDialog.SUCCESS_TYPE
                ).setTitleText("Good job!").setContentText("successful").setConfirmClickListener { sDialog -> // Showing simple toast message to user
                    sDialog.dismissWithAnimation()
                    //  startActivity(Intent(this, MainActivity::class.java))
                }.setConfirmClickListener { sDialog ->
                    sDialog.dismissWithAnimation()
//                    context.startActivity(Intent(context, LoginActivity::class.java))
                    context.startActivity(Intent(context,MainActivity::class.java))

                }.show()

            }
            .addOnFailureListener {
                dismissDialogForLoading()
                // Handle failed upload
                SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
                    .setContentText(it.toString()).show()
            }
    }
    fun likePost(id:String,userId: String,title:String,message:String,token:String){
        DATABASE.child("likes").child(id).child(userId).setValue(userId).addOnSuccessListener {
            PushNotifications(
                NotificationData(title, message),
                token
            ).also {
                sendNotification(it)
            }
        }
    }
    fun disLike(id:String,userId: String){
        DATABASE.child("likes").child(id).child(userId).removeValue()
    }
    fun postComment( postId: String,post: PostModel, context: Context,title: String,message: String,token: String){
        showAlertDialogForLoading(context)
        // Assuming you have a "users" node in your database
        val usersRef = DATABASE.child("comments")

        // Set the user object under the user's ID node
        usersRef.child(postId).child(post.id.toString()).setValue(post)
            .addOnSuccessListener {
                dismissDialogForLoading()
                PushNotifications(
                    NotificationData(title, message),
                    token
                ).also {
                    sendNotification(it)
                }
                SweetAlertDialog(
                    context,
                    SweetAlertDialog.SUCCESS_TYPE
                ).setTitleText("Good job!").setContentText("successful").setConfirmClickListener { sDialog -> // Showing simple toast message to user
                    sDialog.dismissWithAnimation()
                    //  startActivity(Intent(this, MainActivity::class.java))
                }.setConfirmClickListener { sDialog ->
                    sDialog.dismissWithAnimation()
//                    context.startActivity(Intent(context, LoginActivity::class.java))
                    //context.startActivity(Intent(context,MainActivity::class.java))

                }.show()

            }
            .addOnFailureListener {
                dismissDialogForLoading()
                // Handle failed upload
                SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
                    .setContentText(it.toString()).show()
            }
    }
    private fun sendNotification(notification: PushNotifications) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {

            } else {
                Log.e("aaaaaa", response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e("aaaaaa", e.toString())
        }
    }

}
