package com.bhaskardamayanthi.gossy.adapter

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhaskardamayanthi.gossy.databinding.PostBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FirebaseDataManager
import com.bhaskardamayanthi.gossy.managers.GETfromFirebaseManager
import com.bhaskardamayanthi.gossy.model.PostModel
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Locale

class AnonymousPostAdapter(val context:Context):RecyclerView.Adapter<AnonymousPostAdapter.ViewHolder>() {
    private var list: List<PostModel> = emptyList()
    fun setData(newList: List<PostModel>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: PostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storeManager = StoreManager(context)
        val  number = storeManager.getString("number", "")
        val geTfromFirebaseManager = GETfromFirebaseManager()
        val firebaseDataManager = FirebaseDataManager()
        getUserName(list[position].authId.toString()) { fake_name, fake_img ->
            holder.binding.userName.text = fake_name
            Glide.with(context).load(fake_img).into(holder.binding.postProfile)

        }
        holder.binding.postText.text = list[position].postText
        holder.binding.time.text = getRemainingTime(list[position].time.toString())
        geTfromFirebaseManager.getLikeCount(list[position].id.toString()) { count ->
            // Do something with the count, for example, print it
            holder.binding.likeText.text =count.toString()
            // You can perform other operations with the count here
        }
        holder.binding.likeBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                firebaseDataManager.likePost(list[position].id.toString(),number)
            } else {
                // Handle unchecked state if needed
                firebaseDataManager.disLike((list[position].id.toString()),number)
            }
        }
    }


    private fun getUserName(number: String, callback: (name: String, fakeImg: String) -> Unit) {
        val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .reference
            .child("users")
            .child(number.toString())


        database.child("fakeImg").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fakeImg = snapshot.value.toString()

                // Assuming fake name is stored in a different node or child
                val nameReference = database.child("fakeName")


                nameReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(nameSnapshot: DataSnapshot) {
                        val fakeName = nameSnapshot.value.toString()
                        // Callback with fake name and fake image
                        callback(fakeName, fakeImg)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle onCancelled for fake name retrieval
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled for fake image retrieval
            }
        })
    }
    private fun getRemainingTime(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateFormat.parse(dateString)

        val calendarCurrent = Calendar.getInstance()
        val calendarGiven = Calendar.getInstance()
        calendarGiven.time = date

        val currentTimeInMillis = calendarCurrent.timeInMillis
        val givenTimeInMillis = calendarGiven.timeInMillis

        var remainingTimeInMillis =  currentTimeInMillis- givenTimeInMillis

        if (remainingTimeInMillis <= 0) {
            return "Time's up!"
        }

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val remainingDays = remainingTimeInMillis / daysInMilli
        remainingTimeInMillis %= daysInMilli

        val remainingHours = remainingTimeInMillis / hoursInMilli
        remainingTimeInMillis %= hoursInMilli

        val remainingMinutes = remainingTimeInMillis / minutesInMilli
        remainingTimeInMillis %= minutesInMilli

        val remainingSeconds = remainingTimeInMillis / secondsInMilli

        val stringBuilder = StringBuilder()

        if (remainingDays > 0) {
            stringBuilder.append("$remainingDays days, ")
        }

        if (remainingHours > 0 || stringBuilder.isNotEmpty()) {
            stringBuilder.append("$remainingHours hours, ")
        }

        if (remainingMinutes > 0 || stringBuilder.isNotEmpty()) {
            stringBuilder.append("$remainingMinutes minutes, ")
        }

        stringBuilder.append("$remainingSeconds seconds")

        return stringBuilder.toString()
    }


}