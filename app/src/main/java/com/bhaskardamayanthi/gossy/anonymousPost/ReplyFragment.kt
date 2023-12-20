package com.bhaskardamayanthi.gossy.anonymousPost

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.adapter.AnonymousPostAdapter
import com.bhaskardamayanthi.gossy.databinding.FragmentReplyBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FirebaseDataManager
import com.bhaskardamayanthi.gossy.managers.FragmentIntentManager
import com.bhaskardamayanthi.gossy.viewModel.ReplyFragmentViewModel
import com.bhaskardamayanthi.gossy.viewModel.ShareDataInFragmentViewModel
import com.bumptech.glide.Glide


class ReplyFragment : Fragment() {


private lateinit var binding:FragmentReplyBinding
    private lateinit var viewModel:ReplyFragmentViewModel
    private lateinit var shareDataInFragmentViewModel: ShareDataInFragmentViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[ReplyFragmentViewModel::class.java]
        val shareDataInFragmentViewModel = ViewModelProvider(requireActivity())[ShareDataInFragmentViewModel::class.java]
        binding = FragmentReplyBinding.inflate(layoutInflater,container,false)
        val postId = arguments?.getString("id")
        val path = arguments?.getString("path")
        val token = arguments?.getString("token")
        val toNumber =arguments?.getString("number")
        binding.commentBtn.setOnClickListener {
//            val commentFragment = CommentFragment()
//            shareDataInFragmentViewModel.sharedData.value = binding.userName.text.toString()
//            shareDataInFragmentViewModel.getParentPostId.value = postId
//            //     commentFragment.show((context as AppCompatActivity).supportFragmentManager,"Comments")
//
//            //  val bundle = Bundle()
//            //    bundle.putString("userName", name) // Replace "YourDataHere" with the actual data
//
//            //  commentFragment.arguments = bundle
//
//            FragmentIntentManager.intentFragment(R.id.frag, commentFragment, requireContext(), "comment")
            val bundle = Bundle()
            bundle.putString("authId", toNumber)
            bundle.putString("name", binding.userName.text.toString())
            bundle.putString("id", postId)
            bundle.putString("token", token)

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
        }


        val firebaseDataManager = FirebaseDataManager()
        val storeManager = StoreManager(requireContext())
        val  number = storeManager.getString("number", "")
        val userName = storeManager.getString("fakeName","")
        // Inflate the layout for this fragment
            // Toast.makeText(requireContext(), arguments?.getString("name"), Toast.LENGTH_SHORT).show()
        Glide.with(requireActivity()).load(arguments?.getString("fakeImg")).into(binding.postProfile)
        val postText =  arguments?.getString("postText")

        binding.userName.text = arguments?.getString("name")
        binding.postText.text = postText

        binding.likeText.text = arguments?.getString("likes")
        binding.commentsNumber.text = arguments?.getString("comments")
        binding.time.text = arguments?.getString("time")
        val checked = arguments?.getBoolean("isLike")?:false
      //  Toast.makeText(requireContext(), checked.toString(), Toast.LENGTH_SHORT).show()

        binding.likeBtn.isChecked = checked
        viewModel.fetchDataForPost(postId.toString())

        binding.commentsRv.addItemDecoration(DividerItemDecoration(binding.commentsRv.context, DividerItemDecoration.VERTICAL))
        binding.commentsRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = AnonymousPostAdapter(requireContext(),shareDataInFragmentViewModel,false,viewLifecycleOwner)
        binding.commentsRv.adapter = adapter
        viewModel.dataList.observe(viewLifecycleOwner){data->
            adapter.setData(data)
            adapter.type("comment")
            //Toast.makeText(requireContext(), data.toString(), Toast.LENGTH_SHORT).show()
        }
//        binding.likeBtn.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
//                firebaseDataManager.likePost(postId?:"",number,userName+" is liked your post",postText.toString(),token.toString())
//            } else {
//                // Handle unchecked state if needed
//                firebaseDataManager.disLike(postId?:"",number)
//            }
//        }
        binding.likeBtn.setOnClickListener {
            if (binding.likeBtn.isChecked){

                firebaseDataManager.likePost(postId?:"",number,userName+" is liked your post",postText.toString(),token.toString(),toNumber.toString(),path.toString())
            }else{
                firebaseDataManager.disLike(postId?:"",number)
            }
        }
        binding.imageButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }


}