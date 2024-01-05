package com.bhaskardamayanthi.gossy.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
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
import com.bhaskardamayanthi.gossy.notificationSee.CommentActivity
import com.bhaskardamayanthi.gossy.notificationSee.SeeLikePostActivity
import com.bhaskardamayanthi.gossy.viewModel.ShareDataInFragmentViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.Calendar
import java.util.Locale

class AnonymousPostAdapter(val context:Context,val shareDataInFragmentViewModel: ShareDataInFragmentViewModel,val isNotification:Boolean,private val lifecycleOwner: LifecycleOwner):RecyclerView.Adapter<AnonymousPostAdapter.ViewHolder>() {
    private var list: List<PostModel> = emptyList()
    private var imgList = HashMap<Int,String>()
    private var type: String = ""

    private lateinit var name: String
    fun setData(newList: List<PostModel>) {
        list = newList
        notifyDataSetChanged()
    }
    fun type(pType:String){
        type = pType
    }

    inner class ViewHolder(val binding: PostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        imgList.clear()

        val storeManager = StoreManager(context)
       // val token = TokenManager(context)
         name = ""
        val  number = storeManager.getString("number", "")
        val userName = storeManager.getString("fakeName","")

        val geTfromFirebaseManager = GETfromFirebaseManager()
        val firebaseDataManager = FirebaseDataManager()
//        getUserName(list[position].authId.toString()) { fake_name, fake_img ->
//            holder.binding.userName.text = fake_name
//            name = fake_name
//            imgList[position] = fake_img
//            Glide.with(context).load(fake_img).into(holder.binding.postProfile)
//
//
//        }
// Inside a CoroutineScope



        CoroutineScope(Dispatchers.Main).launch {
            try {
                val (fakeName, fakeImg) = getUserName(list[position].authId.toString())
                // Use fakeName and fakeImg here
                holder.binding.userName.text = fakeName
                imgList[position] = fakeImg
                name = fakeName
                Glide.with(context).load(fakeImg).into(holder.binding.postProfile)
                loadImageWithProgressBar(fakeImg,holder.binding)
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }

      //  holder.binding.postText.text = list[position].postText


// Set the text with color-tagged spannable string to the TextView
        holder.binding.postText.text = list[position].postText
        holder.binding.time.text = getRemainingTime(list[position].time.toString())
        geTfromFirebaseManager.getLikeCount(number,list[position].id.toString(),type) { count,isLiked ->
            // Do something with the count, for example, print it
            holder.binding.likeText.text =count.toString()
            holder.binding.likeBtn.isChecked = isLiked

        }
        geTfromFirebaseManager.getCommentsCount(list[position].id.toString()){commentsCounts->

            holder.binding.commentsNumber.text = commentsCounts.toString()
        }
//        holder.binding.likeBtn.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
//                try {
//                    firebaseDataManager.likePost(list[position].id.toString(),number,userName+" liked your post",list[position].postText.toString(),list[position].token.toString())
//
//                }catch (e:Exception){
//
//                }
//            } else {
//                // Handle unchecked state if needed
//                try {
//                    firebaseDataManager.disLike((list[position].id.toString()),number)
//                }catch (e:Exception){
//
//                }
//
//            }
//        }
        holder.binding.likeBtn.setOnClickListener {

            if (holder.binding.likeBtn.isChecked){
                val likebtn = holder.binding.likeBtn
                var likeBtnText = likebtn.text.toString()
                likeBtnText += 1
                holder.binding.likeText.setText(likeBtnText.toString())
                firebaseDataManager.likePost(list[position].id.toString(),number,
                    "$userName liked your post",list[position].postText.toString(),list[position].token.toString(),list[position].authId.toString(),list[position].path.toString())
            }else{
                firebaseDataManager.disLike((list[position].id.toString()),number)
            }
        }
        shareDataInFragmentViewModel.dataUpdated.observe(lifecycleOwner) { isUpdated ->
            if (isUpdated) {
                // Launch the fragment when data is updated
                val commentFragment = CommentFragment()
                intentFragment(R.id.frag, commentFragment, context, "comment")
            }
        }
        holder.binding.commentBtn.setOnClickListener {

            if (!isNotification) {
                val bundle = Bundle()
                bundle.putString("authId", list[position].authId.toString())
                bundle.putString("name", holder.binding.userName.text.toString())
                bundle.putString("id", list[position].id.toString())
                bundle.putString("token", list[position].token.toString())

                // Set the result for the parent fragment
                (context as AppCompatActivity).supportFragmentManager.setFragmentResult("data", bundle)

                // Transition to the CommentFragment
                val fragment = CommentFragment()
                fragment.arguments = bundle // Pass the same bundle to the new fragment if needed

                // Begin a fragment transaction to replace the current fragment with CommentFragment
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.frag, fragment, "comment")
                    .addToBackStack(null) // Optional: Add to back stack if you want fragment navigation
                    .commit()



            //  context.setFragmentResult("requestKey", bundleOf("data" to result))
//                CoroutineScope(Dispatchers.Main).launch {
//                    delay(1000) // Optional delay
//
//                    // Set data in ViewModel
//         //           shareDataInFragmentViewModel.setData(holder.binding.userName.text.toString(),list[position].id.toString(),list[position].token.toString(),list[position].authId.toString())
//                }
//                CoroutineScope(Dispatchers.Main).launch {
//                    delay(1000) // Optional delay
//
//                    // Set data in ViewModel
//                    shareDataInFragmentViewModel.parentPhoneNumber.value = list[position].authId
//                    shareDataInFragmentViewModel.sharedData.value = holder.binding.userName.text.toString()
//                    shareDataInFragmentViewModel.getParentPostId.value = list[position].id
//                    shareDataInFragmentViewModel.getParentTokenId.value = list[position].token
//
//                    // Observe the LiveData changes
//                    shareDataInFragmentViewModel.sharedData.observe(lifecycleOwner) { newData ->
//                        // Data has been updated, now launch the fragment
//                        val commentFragment = CommentFragment()
//                        intentFragment(R.id.frag, commentFragment, context, "comment")
//                    }
//                }
            }
//                shareDataInFragmentViewModel.getParentPostId.value = list[position].id
//                shareDataInFragmentViewModel.getParentTokenId.value = list[position].token
//                shareDataInFragmentViewModel.parentPhoneNumber.value = list[position].authId

                //  commentFragment.show((context as AppCompatActivity).supportFragmentManager,"Comments")


//                val bundle = Bundle().apply {
//                    putString("id",list[position].id)
//                    putString("token",list[position].token)
//                    putString("authId",list[position].authId)
//                }
//                commentFragment.arguments = bundle


            else {
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra("userName",holder.binding.userName.text.toString())
                intent.putExtra("postId", list[position].id)

                intent.putExtra("token",list[position].token.toString())
                context.startActivity(intent)

            }
        }
        holder.binding.postCard.setOnClickListener {


               if (!isNotification) {
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
                       putString("token",list[position].token.toString())
                       putString("path",list[position].path.toString())

                       // Toast.makeText(context, holder.binding.likeBtn.isChecked.toString(), Toast.LENGTH_SHORT).show()

                       // Add more data as needed
                   }
                   val replyFragment = ReplyFragment()
                   replyFragment.arguments = bundle


                   Toast.makeText(context, "noo", Toast.LENGTH_SHORT).show()
                   intentFragment(R.id.frag, replyFragment, context, "replyFragment")
               }else {
                   Toast.makeText(context, "00", Toast.LENGTH_SHORT).show()
                   val intent = Intent(context, SeeLikePostActivity::class.java)
                   intent.putExtra("path", list[position].path)
                   context.startActivity(intent)
               }



        }
    }


//    private fun getUserName(number: String, callback: (name: String, fakeImg: String) -> Unit) {
//        val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/")
//            .reference
//            .child("users")
//            .child(number.toString())
//
//
//        database.child("fakeImg").addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val fakeImg = snapshot.value.toString()
//
//                // Assuming fake name is stored in a different node or child
//                val nameReference = database.child("fakeName")
//
//
//                nameReference.addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(nameSnapshot: DataSnapshot) {
//                        val fakeName = nameSnapshot.value.toString()
//
//                        // Callback with fake name and fake image
//                        callback(fakeName, fakeImg)
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        // Handle onCancelled for fake name retrieval
//                    }
//                })
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle onCancelled for fake image retrieval
//            }
//        })
//    }
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
            return "0 seconds"
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


    suspend fun getFakeImg(database: DatabaseReference): String = withContext(Dispatchers.IO) {
        database.child("fakeImg").get().await().value.toString()
    }

    suspend fun getFakeName(database: DatabaseReference): String = withContext(Dispatchers.IO) {
        database.child("fakeName").get().await().value.toString()
    }

    suspend fun getUserName(number: String): Pair<String, String> = withContext(Dispatchers.IO) {
        val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .reference
            .child("users")
            .child(number)

        val fakeImg = getFakeImg(database)
        val fakeName = getFakeName(database)

        Pair(fakeName, fakeImg)
    }
    fun loadImageWithProgressBar(urlImage: String, binding: PostBinding){
        // Show progress bar

        binding.shemmer.startShimmer()
        Glide.with(context)
            .load(urlImage)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.shemmer.hideShimmer()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.shemmer.hideShimmer()
                    binding.postProfile.background = ColorDrawable(Color.WHITE)
                    return false
                }

            })
            .into(binding.postProfile)
    }
}