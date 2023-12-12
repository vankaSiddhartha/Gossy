package com.bhaskardamayanthi.gossy.auth.userDataFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.adapter.ChooseCollageAdapter
import com.bhaskardamayanthi.gossy.viewModel.SelectCollegeViewModel
import com.bhaskardamayanthi.gossy.databinding.FragmentSelectCollegeBinding
import com.bhaskardamayanthi.gossy.loading.Loading.dismissDialogForLoading
import com.bhaskardamayanthi.gossy.loading.Loading.showAlertDialogForLoading
import com.bhaskardamayanthi.gossy.managers.FragmentIntentManager
import com.bhaskardamayanthi.gossy.model.CollageModel

class SelectCollegeFragment : Fragment() {
private lateinit var binding:FragmentSelectCollegeBinding
private lateinit var chooseCollageviewModel: SelectCollegeViewModel
private lateinit var list:List<CollageModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectCollegeBinding.inflate(layoutInflater,container,false)
        chooseCollageviewModel = ViewModelProvider(requireActivity())[SelectCollegeViewModel::class.java]
        // Inflate the layout for this fragment
        val recyclerView: RecyclerView = binding.chooseCollageRv
        // adding line divider
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        val emptyArrayList = ArrayList<CollageModel>()
        val adapter = ChooseCollageAdapter(requireContext(),emptyArrayList)
        recyclerView.adapter = adapter
        // Observe the loading status
        chooseCollageviewModel.loadingStatus.observe(requireActivity()) { isLoading ->
            if (isLoading) showAlertDialogForLoading(requireActivity()) else dismissDialogForLoading()
        }

        // Observe the collageData LiveData and update the adapter
        chooseCollageviewModel.collageData.observe(requireActivity()) { collages ->
            adapter.updateData(collages)
            list = collages
        }
        binding.searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val filteredList = ArrayList<CollageModel>()
                    for (i in list) {
                        if (i.college!!.contains(newText,ignoreCase = true)) {
                            filteredList.add(i)
                        }
                    }

                    if (filteredList.isNotEmpty()) {
                        binding.chooseCollageRv.adapter =
                            ChooseCollageAdapter(requireContext(),filteredList)
                    }
                }

                return true
            }


        })

        // Fetch data from Firebase
        chooseCollageviewModel.fetchData()
        binding.back.setOnClickListener {
            FragmentIntentManager.intentFragment(
                R.id.fragment_container,
                NameFragment(),
                requireContext()
            )
        }



        return binding.root
    }



}