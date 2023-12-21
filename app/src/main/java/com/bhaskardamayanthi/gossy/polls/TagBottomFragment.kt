package com.bhaskardamayanthi.gossy.polls

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.adapter.ChooseCollageAdapter
import com.bhaskardamayanthi.gossy.adapter.TagFriendAdapter
import com.bhaskardamayanthi.gossy.databinding.FragmentTagBottomBinding
import com.bhaskardamayanthi.gossy.databinding.TagFriendItemBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.model.CollageModel
import com.bhaskardamayanthi.gossy.model.UserModel
import com.bhaskardamayanthi.gossy.viewModel.TagFriendsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class TagBottomFragment : BottomSheetDialogFragment(){

    private lateinit var viewModel: TagFriendsViewModel
    private lateinit var binding: FragmentTagBottomBinding
    private lateinit var list:List<UserModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[TagFriendsViewModel::class.java]
        binding = FragmentTagBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         binding.shimmer.startShimmer()
        val storeManager = StoreManager(requireContext())
        val userId = storeManager.getString("number", "")
        binding.findFriendRv.layoutManager = LinearLayoutManager(requireContext())
        val receivedData = arguments?.getString("question")
        val adapter = TagFriendAdapter(requireContext())


        binding.findFriendRv.apply {
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )


        }
        binding.findFriendRv.adapter = adapter

        viewModel.getFriendsData(userId)

        viewModel.getFriendData.observe(viewLifecycleOwner) { friendsData ->
            if (friendsData.size==0){
                binding.frindText.visibility = View.VISIBLE
            }
            adapter.addNewData(friendsData)
            adapter.getQuestion(receivedData.toString())
            list = friendsData
          //  Toast.makeText(requireContext(), friendsData.toString(), Toast.LENGTH_SHORT).show()
           // Log.e("vankaIsGoat", friendsData.toString())
        }
        viewModel.isLoding.observe(requireActivity()){isLoading->
            if (!isLoading){
               binding.shimmer.hideShimmer()
                binding.findFriendRv.visibility = View.VISIBLE
                binding.shimmer.visibility = View.GONE
            }

        }
        binding.searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val filteredList = ArrayList<UserModel>()
                    for (i in list) {
                        if (i.name!!.contains(newText,ignoreCase = true)) {
                            filteredList.add(i)
                        }
                    }

                    if (filteredList.isNotEmpty()) {
                        adapter.addNewData(filteredList)
                    }
                }

                return true
            }


        })
    }
}
