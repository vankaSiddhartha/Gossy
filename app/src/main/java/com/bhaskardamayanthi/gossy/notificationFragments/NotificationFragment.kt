package com.bhaskardamayanthi.gossy.notificationFragments

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
import com.bhaskardamayanthi.gossy.adapter.NotificationAdapter
import com.bhaskardamayanthi.gossy.databinding.FragmentAnonymousPostBinding
import com.bhaskardamayanthi.gossy.databinding.FragmentNotificationBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.model.NotificationModel
import com.bhaskardamayanthi.gossy.viewModel.NotificationViewModel


class NotificationFragment : Fragment() {
    private lateinit var binding:FragmentNotificationBinding
    private lateinit var viewModel:NotificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding = FragmentNotificationBinding.inflate(layoutInflater,container,false)
        binding.shimmer.startShimmer()
        viewModel = ViewModelProvider(requireActivity())[NotificationViewModel::class.java]
        val number = StoreManager(requireContext()).getString("number","")
        val adapter = NotificationAdapter(requireContext())

        viewModel.fetchData(number)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(binding.recyclerView.context, DividerItemDecoration.VERTICAL))
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = true // Reverse the layout
        layoutManager.stackFromEnd = true // Display items from the end
        binding.recyclerView.layoutManager= layoutManager
        binding.recyclerView.adapter = adapter
        viewModel.liveData.observe(viewLifecycleOwner){newData->
            adapter.updateData(newData)
        }
        viewModel.isLoading.observe(requireActivity()){isLoading->
            if (!isLoading){
                binding.shimmer.hideShimmer()
                binding.recyclerView.visibility = View.VISIBLE
                binding.shimmer.visibility = View.GONE
            }

        }
        return binding.root
    }


}