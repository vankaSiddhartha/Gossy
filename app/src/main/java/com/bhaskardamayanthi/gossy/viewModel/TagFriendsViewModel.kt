package com.bhaskardamayanthi.gossy.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhaskardamayanthi.gossy.adapter.TagFriendAdapter
import com.bhaskardamayanthi.gossy.model.FriendsModel
import com.bhaskardamayanthi.gossy.model.UserModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TagFriendsViewModel : ViewModel() {
    private val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
    val liveFriendIdData = MutableLiveData<MutableList<FriendsModel>>()
    val getFriendData = MutableLiveData<MutableList<UserModel>>()
    val isLoding = MutableLiveData<Boolean>()

    fun getFriendsData(userId: String) {
        isLoding.value = true
        val getData = mutableListOf<UserModel>()

        database.child("friends").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(friendsSnapshot: DataSnapshot) {
                val friendIds = mutableListOf<String>()
                for (friendSnapshot in friendsSnapshot.children) {
                    val friendId = friendSnapshot.getValue(FriendsModel::class.java)?.friendId
                    friendId?.let { friendIds.add(it) }
                }
                fetchUsersData(friendIds)
            }

            override fun onCancelled(error: DatabaseError) {
                isLoding.value = false
            }
        })
    }

    private fun fetchUsersData(friendIds: List<String>) {
        val getData = mutableListOf<UserModel>()
        val tasks = mutableListOf<Task<DataSnapshot>>()

        friendIds.forEach { friendId ->
            val userTask = database.child("users").child(friendId).get()
            tasks.add(userTask)
        }

        Tasks.whenAllComplete(tasks)
            .addOnSuccessListener { taskList ->
                taskList.forEach { task ->
                    if (task.isSuccessful) {
                        val userSnapshot = task.result as DataSnapshot
                        val user = userSnapshot.getValue(UserModel::class.java)
                        user?.let { getData.add(it) }
                    }
                }
                getFriendData.value = getData
                isLoding.value = false
            }
            .addOnFailureListener { exception ->
                // Handle failure
                isLoding.value = false
            }
    }
}
