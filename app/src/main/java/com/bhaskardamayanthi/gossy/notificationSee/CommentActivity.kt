package com.bhaskardamayanthi.gossy.notificationSee

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivityCommmentBinding
import com.bhaskardamayanthi.gossy.databinding.FragmentCommentBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FirebaseDataManager
import com.bhaskardamayanthi.gossy.managers.TokenManager
import com.bhaskardamayanthi.gossy.model.PostModel
import com.bhaskardamayanthi.gossy.viewModel.ShareDataInFragmentViewModel
import com.bumptech.glide.Glide
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class CommentActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCommmentBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val storeManager = StoreManager(this)
        val userToken = TokenManager(this).getSavedToken()
        val userFakeProfile = storeManager.getString("fakeImg","")
        val number = storeManager.getString("number","0")
        val userName = storeManager.getString("fakeName","")
        val firebaseDataManager = FirebaseDataManager()
        Glide.with(this).load(userFakeProfile).into(binding.commentProfile)
        val fakeName = intent.getStringExtra("userName")
        val getParentId = intent.getStringExtra("postId")
        val parentNumber = intent.getStringExtra("number")
        val token = intent.getStringExtra("token")
        binding.replyTextView.setText( "Replying to @"+fakeName)
//        shareDataInFragmentViewModel.sharedData.observe(this){fakeName->
//
//            parentFakeName = fakeName
//
//
//        }
//        shareDataInFragmentViewModel.getParentPostId.observe(this){getParentPostId->
//            parentId = getParentPostId
//        }
//        shareDataInFragmentViewModel.getParentTokenId.observe(this){getParentToken->
//            parentToken = getParentToken
//
//        }
        binding.cancelBtn.setOnClickListener {

//            val fm: FragmentManager = requireActivity().supportFragmentManager
//            fm.popBackStack()

        }
        binding.replyBtn.setOnClickListener {
            if (binding.commentEt.text.toString().isNotEmpty()) {

                val commentId = UUID.randomUUID().toString()
                val path = "comments/$getParentId/$commentId"
                val comment = PostModel("@"+fakeName+" "+binding.commentEt.text.toString(),0,0,getCurrentDateTime(),commentId,number,userToken,path)
                firebaseDataManager.postComment(getParentId.toString(),comment,this,userName+" is commented on your post",binding.commentEt.text.toString(),token.toString(),path,parentNumber?:"123")
            }else{
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("empty fields").setContentText("Please fill").show()
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Customize the format as needed
        return currentDateTime.format(formatter)

    }
}