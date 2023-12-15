package com.bhaskardamayanthi.gossy.adapter

import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.anonymousPost.CommentFragment
import com.bhaskardamayanthi.gossy.anonymousPost.ReplyFragment
import com.bhaskardamayanthi.gossy.databinding.PostBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FirebaseDataManager
import com.bhaskardamayanthi.gossy.managers.FragmentIntentManager.intentFragment
import com.bhaskardamayanthi.gossy.managers.GETfromFirebaseManager
import com.bhaskardamayanthi.gossy.model.PostModel
import com.bhaskardamayanthi.gossy.viewModel.ShareDataInFragmentViewModel
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Locale

class AnonymousPostAdapter(val context:Context,val shareDataInFragmentViewModel: ShareDataInFragmentViewModel):RecyclerView.Adapter<AnonymousPostAdapter.ViewHolder>() {
    private var list: List<PostModel> = emptyList()
    private var imgList = HashMap<Int,String>()

    private lateinit var name: String
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
        imgList.clear()

        val storeManager = StoreManager(context)
         name = ""
        val  number = storeManager.getString("number", "")
        val geTfromFirebaseManager = GETfromFirebaseManager()
        val firebaseDataManager = FirebaseDataManager()
        getUserName(list[position].authId.toString()) { fake_name, fake_img ->
            holder.binding.userName.text = fake_name
            name = fake_name
            imgList[position] = fake_img
            Glide.with(context).load(fake_img).into(holder.binding.postProfile)


        }

      //  holder.binding.postText.text = list[position].postText
        val pattern = "@[a-zA-Z0-9_]+".toRegex() // Define the pattern to find usernames starting with @

        val spannableString = SpannableString(list[position].postText)

        pattern.findAll(list[position].postText.toString()).forEach { matchResult ->
            val startIndex = matchResult.range.first
            val endIndex = matchResult.range.last + 1 // Adding 1 to include '@' in the span

            spannableString.setSpan(
                ForegroundColorSpan(Color.BLUE), // Set the color
                startIndex, // Start index of the @ tag
                endIndex, // End index of the @ tag (inclusive)
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

// Set the text with color-tagged spannable string to the TextView
        holder.binding.postText.text = spannableString
        holder.binding.time.text = getRemainingTime(list[position].time.toString())
        geTfromFirebaseManager.getLikeCount(number,list[position].id.toString()) { count,isLiked ->
            // Do something with the count, for example, print it
            holder.binding.likeText.text =count.toString()
            holder.binding.likeBtn.isChecked = isLiked

        }
        geTfromFirebaseManager.getCommentsCount(list[position].id.toString()){commentsCounts->

            holder.binding.commentsNumber.text = commentsCounts.toString()
        }
        holder.binding.likeBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                try {
                    firebaseDataManager.likePost(list[position].id.toString(),number)

                }catch (e:Exception){

                }
            } else {
                // Handle unchecked state if needed
                try {
                    firebaseDataManager.disLike((list[position].id.toString()),number)
                }catch (e:Exception){

                }

            }
        }
        holder.binding.commentBtn.setOnClickListener {
            val commentFragment = CommentFragment()
            shareDataInFragmentViewModel.sharedData.value = holder.binding.userName.text.toString()
            shareDataInFragmentViewModel.getParentPostId.value = list[position].id
       //     commentFragment.show((context as AppCompatActivity).supportFragmentManager,"Comments")

          //  val bundle = Bundle()
        //    bundle.putString("userName", name) // Replace "YourDataHere" with the actual data

          //  commentFragment.arguments = bundle

            intentFragment(R.id.frag, commentFragment, context,"comment")
        }
        holder.binding.postCard.setOnClickListener {
            val bundle = Bundle().apply {
                putString("name", holder.binding.userName.text.toString())
                putString("number",list[position].authId)
                putString("time",holder.binding.time.text.toString())
                putString("id",list[position].id)
                putString("postText",list[position].postText)
                putString("likes",holder.binding.likeText.text.toString())
                putString("comments",holder.binding.commentsNumber.text.toString())
                putString("fakeImg", imgList[position])
                putBoolean("isLike",holder.binding.likeBtn.isChecked)
               // Toast.makeText(context, holder.binding.likeBtn.isChecked.toString(), Toast.LENGTH_SHORT).show()

                // Add more data as needed
            }
            val replyFragment = ReplyFragment()
            replyFragment.arguments = bundle
            intentFragment(R.id.frag,replyFragment,context,"replyFragment")

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