package com.bhaskardamayanthi.gossy.anonymousPost

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
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


class CommentFragment : Fragment() {
    private lateinit var binding:FragmentCommentBinding
    private lateinit var parentId:String
   private lateinit var parentFakeName:String
   private lateinit var parentToken:String
   private lateinit var parentPhoneNumber:String


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        parentFakeName =""
        parentToken =""
        parentId =""
        parentPhoneNumber = ""
        parentFragmentManager.setFragmentResultListener("data", this) { requestKey, bundle ->
            // Handle the received data here
            val authId = bundle.getString("authId")
            val name = bundle.getString("name")
            val id = bundle.getString("id")
            val token = bundle.getString("token")
            parentFakeName = name.toString()
            parentToken = token.toString()
            parentId = id.toString()
            parentPhoneNumber = authId.toString()



        }

   //     Toast.makeText(requireContext(), "hi", Toast.LENGTH_SHORT).show()
        val shareDataInFragmentViewModel = ViewModelProvider(requireActivity())[ShareDataInFragmentViewModel::class.java]
        binding = FragmentCommentBinding.inflate(layoutInflater,container,false)
        val authId = arguments?.getString("authId")
        val token = arguments?.getString("token")
        val id = arguments?.getString("id")
//        parentFragmentManager.setFragmentResultListener(
//            "data", viewLifecycleOwner
//        ) { requestKey: String?, result: Bundle ->
//            val value1 = result.getString("id")
//            val value2 = result.getString("token")
//            val value3 = result.getString("authId")
//            val value4 = result.getString("token")
//
//            Toast.makeText(requireContext(), "$value1", Toast.LENGTH_SHORT).show()
//        }
        // Toast.makeText(context, authId.toString(), Toast.LENGTH_SHORT).show()
        val storeManager = StoreManager(requireContext())
        val userToken = TokenManager(requireContext()).getSavedToken()
        val userFakeProfile = storeManager.getString("fakeImg","")
        val number = storeManager.getString("number","0")
        val userName = storeManager.getString("fakeName","")
        val firebaseDataManager = FirebaseDataManager()
        Glide.with(requireContext()).load(userFakeProfile).into(binding.commentProfile)
        binding.replyBtn.setOnClickListener {

//            Toast.makeText(requireContext(), "ssd", Toast.LENGTH_SHORT).show()
//            parentFragmentManager.setFragmentResultListener(
//                "data", viewLifecycleOwner
//            ) { requestKey: String?, result: Bundle ->
//                val value1 = result.getString("id")
//                val value2 = result.getString("token")
//                val value3 = result.getString("authId")
//                val value4 = result.getString("token")
//
//                Toast.makeText(requireContext(), "$value1+hggv", Toast.LENGTH_SHORT).show()
//            }
//            shareDataInFragmentViewModel.parentPhoneNumber.observe(viewLifecycleOwner){phoneNo->
//                parentPhoneNumber = phoneNo
//                Toast.makeText(requireContext(), "$phoneNo sss", Toast.LENGTH_SHORT).show()
//
//            }
        }

//        shareDataInFragmentViewModel.sharedData.observe(viewLifecycleOwner){fakeName->
//            binding.replyTextView.setText( "Replying to @"+fakeName)
//            parentFakeName = fakeName
//
//
//        }
//        shareDataInFragmentViewModel.getParentPostId.observe(viewLifecycleOwner){getParentPostId->
//            parentId = getParentPostId
//        }
//        shareDataInFragmentViewModel.getParentTokenId.observe(viewLifecycleOwner){getParentToken->
//            parentToken = getParentToken
//
//        }
        binding.cancelBtn.setOnClickListener {

            val fm: FragmentManager = requireActivity().supportFragmentManager
            fm.popBackStack()

        }
        binding.replyBtn.setOnClickListener {
        if (binding.commentEt.text.toString().isNotEmpty()) {

            val commentId = UUID.randomUUID().toString()
            val path = "comments/$id/$commentId"
            val comment = PostModel("@"+parentFakeName+" "+binding.commentEt.text.toString(),0,0,getCurrentDateTime(),commentId,number,userToken,path)
            firebaseDataManager.postComment(parentId,comment,requireContext(),userName+" is commented on your post",binding.commentEt.text.toString(),parentToken,path,authId.toString())
        }else{
            SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("empty fields").setContentText("Please fill").show()
        }
//            if (binding.commentEt.text.toString().isNotEmpty()) {
//
//                val commentId = UUID.randomUUID().toString()
//         //       val path = parentId+"/"+commentId
//              val path = "comments/$parentId/$commentId"
//                val comment = PostModel("@"+parentFakeName+" "+binding.commentEt.text.toString(),0,0,getCurrentDateTime(),commentId,number,userToken,path)
//                firebaseDataManager.postComment(parentId,comment,requireContext(),userName+" is commented on your post",binding.commentEt.text.toString(),parentToken,path,parentPhoneNumber)
//            }else{
//                SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("empty fields").setContentText("Please fill").show()
//            }
//        }
        }
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Customize the format as needed
        return currentDateTime.format(formatter)

    }

}