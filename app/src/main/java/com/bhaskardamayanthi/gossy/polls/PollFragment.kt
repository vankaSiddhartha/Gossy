package com.bhaskardamayanthi.gossy.polls

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.adapter.PollAdapter
import com.bhaskardamayanthi.gossy.databinding.FragmentPollBinding
import com.bhaskardamayanthi.gossy.viewModel.PollsViewMode

class PollFragment : Fragment() {
private lateinit var binding:FragmentPollBinding
private lateinit var viewModel:PollsViewMode
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPollBinding.inflate(layoutInflater,container,false)
        viewModel = ViewModelProvider(requireActivity())[PollsViewMode::class.java]
        val adapter = PollAdapter(requireContext())
        binding.pollsVp.adapter = adapter
        viewModel.getQuestionData.observe(viewLifecycleOwner){data->
            adapter.updateList(data)
            //Toast.makeText(requireContext(),data.toString(), Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }


}