package com.bhaskardamayanthi.gossy.trending

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.adapter.AnonymousPostAdapter
import com.bhaskardamayanthi.gossy.databinding.FragmentAnonymousPostBinding
import com.bhaskardamayanthi.gossy.databinding.FragmentTrendingBinding
import com.bhaskardamayanthi.gossy.viewModel.AnonymousPostViewModel
import com.bhaskardamayanthi.gossy.viewModel.ShareDataInFragmentViewModel
import com.bhaskardamayanthi.gossy.viewModel.TrendingViewModel


class TrendingFragment : Fragment() {
private lateinit var binding:FragmentTrendingBinding
private lateinit var viewModel:TrendingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTrendingBinding.inflate(layoutInflater,container,false)
        viewModel = ViewModelProvider(requireActivity())[TrendingViewModel::class.java]
        val shareDataInFragmentViewModel = ViewModelProvider(requireActivity())[ShareDataInFragmentViewModel::class.java]
        val adapter = AnonymousPostAdapter(requireContext(),shareDataInFragmentViewModel,false,viewLifecycleOwner)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(binding.recyclerView.context, DividerItemDecoration.VERTICAL))
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.shimmer.startShimmer()

        viewModel.dataList.observe(requireActivity()) { data ->
            val sortedByLikesDescending =    data.sortedByDescending { it.likes }

            adapter.setData(sortedByLikesDescending)



        }
        viewModel.isLoading.observe(requireActivity()){isLoding->
            if (!isLoding){
                binding.shimmer.hideShimmer()
                binding.recyclerView.visibility = View.VISIBLE
                binding.shimmer.visibility = View.GONE
            }

        }
        viewModel.fetch(requireActivity())
        return binding.root
    }

}