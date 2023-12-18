package com.bhaskardamayanthi.gossy.viewModel

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bhaskardamayanthi.gossy.adapter.TagFriendAdapter
import com.bhaskardamayanthi.gossy.model.QuestionModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class PollsViewMode: ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate = LocalDate.now().toString()
    @RequiresApi(Build.VERSION_CODES.O)
    private val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("polls").child(currentDate)
    val isLoading = MutableLiveData<Boolean>()
    val getQuestionData = MutableLiveData<MutableList<QuestionModel>>()
  init {

      getQuestions()
  }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getQuestions(){
        isLoading.value= false

        val data = mutableListOf<QuestionModel>()
        database.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                val getData = snapshot.getValue(QuestionModel::class.java)
                if (getData != null) {
                    data.add(getData)
                }
                Log.e("vankaSiddhartha",snapshot.toString())
            }
                getQuestionData.value = data

            }


            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}