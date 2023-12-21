package com.bhaskardamayanthi.gossy.anonymousPost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.adapter.AnonymousPostAdapter
import com.bhaskardamayanthi.gossy.databinding.FragmentAnonymousPostBinding
import com.bhaskardamayanthi.gossy.viewModel.AnonymousPostViewModel
import com.bhaskardamayanthi.gossy.viewModel.ShareDataInFragmentViewModel

class AnonymousPostFragment : Fragment() {
private lateinit var binding:FragmentAnonymousPostBinding
private lateinit var viewModel: AnonymousPostViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(requireActivity())[AnonymousPostViewModel::class.java]
        val shareDataInFragmentViewModel = ViewModelProvider(requireActivity())[ShareDataInFragmentViewModel::class.java]
        binding = FragmentAnonymousPostBinding.inflate(layoutInflater,container,false)
        binding.shimmer.startShimmer()
        val adapter = AnonymousPostAdapter(requireContext(),shareDataInFragmentViewModel,false,viewLifecycleOwner)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(binding.recyclerView.context, DividerItemDecoration.VERTICAL))
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.dataList.observe(requireActivity()) { data ->

            adapter.setData(data.reversed())
            adapter.type("post")



        }
        viewModel.isLoading.observe(requireActivity()){isLoding->
            if (!isLoding){
                binding.shimmer.hideShimmer()
                binding.recyclerView.visibility = View.VISIBLE
                binding.shimmer.visibility = View.GONE
            }

        }

        return binding.root
    }


}