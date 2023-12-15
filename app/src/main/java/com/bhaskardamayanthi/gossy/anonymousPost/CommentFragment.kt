package com.bhaskardamayanthi.gossy.anonymousPost

import android.os.Build
import android.os.Bundle
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
import com.bhaskardamayanthi.gossy.model.PostModel
import com.bhaskardamayanthi.gossy.viewModel.ShareDataInFragmentViewModel
import com.bumptech.glide.Glide
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID


class CommentFragment : Fragment() {
    private lateinit var binding:FragmentCommentBinding
    private lateinit var parentId:String
   private lateinit var userFakeName:String


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        userFakeName =""
        val shareDataInFragmentViewModel = ViewModelProvider(requireActivity())[ShareDataInFragmentViewModel::class.java]
        binding = FragmentCommentBinding.inflate(layoutInflater,container,false)
        val storeManager = StoreManager(requireContext())
        val userFakeProfile = storeManager.getString("fakeImg","")
        val number = storeManager.getString("number","0")
        val firebaseDataManager = FirebaseDataManager()
        Glide.with(requireContext()).load(userFakeProfile).into(binding.commentProfile)
        shareDataInFragmentViewModel.sharedData.observe(viewLifecycleOwner){fakeName->
            binding.replyTextView.setText( "Replying to @"+fakeName)
            userFakeName = fakeName


        }
        shareDataInFragmentViewModel.getParentPostId.observe(viewLifecycleOwner){parentPostId->
            parentId = parentPostId
        }

        binding.cancelBtn.setOnClickListener {

            val fm: FragmentManager = requireActivity().supportFragmentManager
            fm.popBackStack()

        }
        binding.replyBtn.setOnClickListener {
        if (binding.commentEt.text.toString().isNotEmpty()) {
            val comment = PostModel("@"+userFakeName+" "+binding.commentEt.text.toString(),0,0,getCurrentDateTime(),UUID.randomUUID().toString(),number)
            firebaseDataManager.postComment(parentId,comment,requireContext())
        }else{
            SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("empty fields").setContentText("Please fill").show()
        }
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