package com.bhaskardamayanthi.gossy.managers

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bhaskardamayanthi.gossy.MainActivity
import com.bhaskardamayanthi.gossy.auth.PermissionActivity
import com.bhaskardamayanthi.gossy.loading.Loading.dismissDialogForLoading
import com.bhaskardamayanthi.gossy.loading.Loading.showAlertDialogForLoading
import com.bhaskardamayanthi.gossy.model.FriendsModel
import com.bhaskardamayanthi.gossy.model.NotificationModel
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
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
    fun likePost(id:String,userId: String,title:String,message:String,token:String,toNumber:String,path: String){
        DATABASE.child("likes").child(id).child(userId).setValue(userId).addOnSuccessListener {
//            PushNotifications(
//                NotificationData(title, message),
//                token
//            ).also {
//                sendNotification(it)
//            }
            sendPollAndLikeAndPostNotifications(path,toNumber,"like",title,message,token)
        }
    }
    fun disLike(id:String,userId: String){
        DATABASE.child("likes").child(id).child(userId).removeValue()
    }
    fun postComment( postId: String,post: PostModel, context: Context,title: String,message: String,token: String,path:String,parentNumber:String){
        showAlertDialogForLoading(context)
        // Assuming you have a "users" node in your database
        val usersRef = DATABASE.child("comments")

        // Set the user object under the user's ID node
        usersRef.child(postId).child(post.id.toString()).setValue(post)
            .addOnSuccessListener {
                dismissDialogForLoading()
//                PushNotifications(
//                    NotificationData(title, message),
//                    token
//                ).also {
//                    sendNotification(it)
//                }
                sendPollAndLikeAndPostNotifications(path,parentNumber,"comment",title,message,token)
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
     fun addFriends(userId: String,friendsId:String,context: Context){
        val data = FriendsModel(friendsId)
        val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        DATABASE.child("friends").child(userId).child(friendsId).setValue(data).addOnSuccessListener {
            SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Good job")
                .setContentText("Nice you have new friend").show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendPollAndLikeAndPostNotifications(userId: String, friendsId: String, type:String, title: String, message: String, token: String){
        val geTfromFirebaseManager = GETfromFirebaseManager()
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        Log.e("NumberBro",friendsId)
        val formattedDateTime = currentDateTime.format(formatter)
        val notificationId =UUID.randomUUID().toString()
        val data = NotificationModel(notificationId,type,title,message,friendsId,userId,formattedDateTime)
        val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("notifications")
        DATABASE.child(friendsId).child(notificationId).setValue(data).addOnSuccessListener {

            PushNotifications(
                NotificationData(title, message),
                token
            ).also {
                sendNotification(it)
            }

        }

    }



}
