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
import androidx.recyclerview.widget.RecyclerView
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.adapter.AnonymousPostAdapter
import com.bhaskardamayanthi.gossy.databinding.FragmentAnonymousPostBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.viewModel.AnonymousPostViewModel
import com.bhaskardamayanthi.gossy.viewModel.FirebasePagingViewModel
import com.bhaskardamayanthi.gossy.viewModel.ShareDataInFragmentViewModel


class AnonymousPostFragment : Fragment() {
    private lateinit var binding:FragmentAnonymousPostBinding
    private lateinit var viewModel: AnonymousPostViewModel
    private lateinit var paging:FirebasePagingViewModel
    private var isLoading = false
    private val visibleThreshold = 5
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val storeManager = StoreManager(requireContext())
        val college = storeManager.getString("college","Bro")
        paging = ViewModelProvider(requireActivity())[FirebasePagingViewModel::class.java]
        viewModel = ViewModelProvider(requireActivity())[AnonymousPostViewModel::class.java]
        val shareDataInFragmentViewModel = ViewModelProvider(requireActivity())[ShareDataInFragmentViewModel::class.java]
        binding = FragmentAnonymousPostBinding.inflate(layoutInflater,container,false)
        binding.shimmer.startShimmer()
        var adapter = AnonymousPostAdapter(requireContext(),shareDataInFragmentViewModel,false,viewLifecycleOwner)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(binding.recyclerView.context, DividerItemDecoration.VERTICAL))
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AnonymousPostAdapter(requireContext(), shareDataInFragmentViewModel, false, viewLifecycleOwner)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

          //  var lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
//            adapter.addLoadStateListener { loadState ->
//                val isEndOfList = loadState.source.refresh is LoadState.NotLoading && // Not currently loading
//                        loadState.append.endOfPaginationReached // Reached the end of data
//
//                if (isEndOfList) {
//                    // You have scrolled to the last page
//                    // Perform actions as needed
//                }
//            }


            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                 //   Toast.makeText(requireContext(), "${dx} $dy", Toast.LENGTH_SHORT).show()
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                //   Toast.makeText(requireContext(), "$totalItemCount $lastVisibleItem", Toast.LENGTH_SHORT).show()

//                    if (!isLoading && totalItemCount == lastVisibleItem+1) {
//                        paging.loadNextPage(requireContext())
//                        isLoading = true
//                    }
                    if (lastVisibleItem == totalItemCount - 1) {
                        Toast.makeText(requireContext(), "loading post..", Toast.LENGTH_SHORT).show()
                        paging.loadNextPage(college,requireContext())
                    }
                }
            })
        }

        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        paging.dataList.observe(requireActivity()) { data ->

            adapter.setData(data.reversed())
            adapter.type("post")



        }
        paging.isLoading.observe(requireActivity()){isLoding->
            if (!isLoding){
                binding.shimmer.hideShimmer()
                binding.recyclerView.visibility = View.VISIBLE
                binding.shimmer.visibility = View.GONE
            }

        }
        paging.load(college,requireContext())

        return binding.root
    }


}