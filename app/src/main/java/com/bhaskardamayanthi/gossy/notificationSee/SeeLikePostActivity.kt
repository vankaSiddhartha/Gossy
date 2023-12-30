package com.bhaskardamayanthi.gossy.notificationSee

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhaskardamayanthi.gossy.MainActivity
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.adapter.AnonymousPostAdapter
import com.bhaskardamayanthi.gossy.anonymousPost.CommentFragment
import com.bhaskardamayanthi.gossy.databinding.ActivitySeeLikePostBinding
import com.bhaskardamayanthi.gossy.databinding.FragmentReplyBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FirebaseDataManager
import com.bhaskardamayanthi.gossy.managers.FragmentIntentManager
import com.bhaskardamayanthi.gossy.managers.GETfromFirebaseManager
import com.bhaskardamayanthi.gossy.model.PostModel
import com.bhaskardamayanthi.gossy.viewModel.ReplyFragmentViewModel
import com.bhaskardamayanthi.gossy.viewModel.ShareDataInFragmentViewModel
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SeeLikePostActivity : AppCompatActivity(){
    private val lifecycleRegistry = LifecycleRegistry(this)


    private lateinit var binding:ActivitySeeLikePostBinding
    private lateinit var viewModel: ReplyFragmentViewModel
    var storedData: PostModel? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val geTfromFirebaseManager = GETfromFirebaseManager()
         viewModel = ViewModelProvider(this)[ReplyFragmentViewModel::class.java]
        binding = ActivitySeeLikePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
             intent.putExtra("key","noti")
            startActivity(intent)
        }
        val firebaseDataManager = FirebaseDataManager()
        val storeManager = StoreManager(this)
        val number = storeManager.getString("number", "")
        val path = intent.getStringExtra("path").toString()
        var type = ""
        type = if (path.contains("post")) {
            "post"
        } else {
            "comments"
        }
        val postId = path.substringAfterLast("/")
        val userName = storeManager.getString("fakeName", "")
         viewModel.fetchDataForPost(postId)

        seePost(path, number) { data ->
            storedData = data
            getUserName(storedData?.authId.toString()) { name, fakeImg ->
                binding.userName.text = name
                Glide.with(this@SeeLikePostActivity).load(fakeImg).into(binding.postProfile)
                binding.likeText.text = storedData?.likes.toString()
                binding.postText.text = storedData?.postText.toString()
                binding.commentsNumber.text = storedData?.comment.toString()
                geTfromFirebaseManager.getLikeCount(
                    number,
                    storedData?.id.toString(),
                    type
                ) { count, isLiked ->

                    binding.likeText.text = count.toString()
                    binding.likeBtn.isChecked = isLiked

                }
                geTfromFirebaseManager.getCommentsCount(storedData?.id.toString()) { commentsCounts ->

                    binding.commentsNumber.text = commentsCounts.toString()
                }

            }
        }

        var likeBtnText = binding.likeText.text.toString().toIntOrNull() ?: 0
        binding.likeBtn.setOnClickListener {
            if (binding.likeBtn.isChecked) {

                likeBtnText += 1


                binding.likeText.text =
                    likeBtnText.toString() // Update the likeText to display the incremented value


                firebaseDataManager.likePost(
                    postId ?: "",
                    number,
                    "$userName is liked your post",
                    storedData?.postText.toString(),
                    storedData?.token.toString(),
                    storedData?.authId.toString(),
                    storedData?.path.toString()
                )
            } else {
                Toast.makeText(this, storedData?.path.toString() , Toast.LENGTH_SHORT).show()

                firebaseDataManager.disLike(
                    postId,
                    storedData?.authId.toString()
                )
            }


        }







        val shareDataInFragmentViewModel = ViewModelProvider(this)[ShareDataInFragmentViewModel::class.java]
        binding.commentBtn.setOnClickListener {
//
//            shareDataInFragmentViewModel.sharedData.value = binding.userName.text.toString()
//            shareDataInFragmentViewModel.getParentPostId.value = postId
            //     commentFragment.show((context as AppCompatActivity).supportFragmentManager,"Comments")

            //  val bundle = Bundle()
            //    bundle.putString("userName", name) // Replace "YourDataHere" with the actual data

            //  commentFragment.arguments = bundle
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("userName",binding.userName.text.toString())
            intent.putExtra("postId", postId)
            intent.putExtra("number",storedData?.authId)

            intent.putExtra("token",storedData?.token.toString())
            startActivity(intent)
        }
        binding.commentsRv.addItemDecoration(DividerItemDecoration(binding.commentsRv.context, DividerItemDecoration.VERTICAL))
        binding.commentsRv.layoutManager = LinearLayoutManager(this)
        val adapter = AnonymousPostAdapter(this,shareDataInFragmentViewModel,true,this)
        binding.commentsRv.adapter = adapter
        viewModel.dataList.observe(this){data->
            adapter.setData(data)
            adapter.type("comment")
            //Toast.makeText(requireContext(), data.toString(), Toast.LENGTH_SHORT).show()
        }


    }

   private fun seePost(path:String,number: String, callback: (PostModel?) -> Unit){


//       val geTfromFirebaseManager = GETfromFirebaseManager()
       val DATABASE = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
       DATABASE.child(path).addListenerForSingleValueEvent(object :ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               val postData = snapshot.getValue(PostModel::class.java)
               callback(postData)

           //    postId = postData?.id.toString()
             //  getUserName(postData?.authId.toString()){
//               }
//               geTfromFirebaseManager.getLikeCount(number,postData?.id.toString(),type){count,isLiked ->
//                   // Do something with the count, for example, print it
//                   binding.likeText.text =count.toString()
//                   binding.likeBtn.isChecked = isLiked
//                 //  binding.commentsNumber.text = commentsCounts.toString()
//               }

           }

           override fun onCancelled(error: DatabaseError) {

           }

       })
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

}