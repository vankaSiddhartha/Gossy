package com.bhaskardamayanthi.gossy.auth.userDataFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.auth.PhoneAuthActivity
import com.bhaskardamayanthi.gossy.databinding.ActivityGenderChooseFragmentBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FragmentIntentManager.intentFragment

class GenderChooseFragment : Fragment() {
private lateinit var binding: ActivityGenderChooseFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = ActivityGenderChooseFragmentBinding.inflate(layoutInflater,container,false)

        binding.male.setOnClickListener {
            storeGender("male")
        }
        binding.female.setOnClickListener {
            storeGender("female")
        }
        binding.nonBinary.setOnClickListener {
            storeGender("nonBinary")
        }
        binding.back.setOnClickListener {
            intentFragment(R.id.fragment_container,DOBYearPickerFragment(),requireContext())
        }

        return binding.root
    }

    private fun storeGender(str:String){
        val storeManager = StoreManager(requireContext())
        storeManager.saveString("sex",str)
        intentFragment(R.id.fragment_container,SelectCollegeFragment(),requireContext())
    }
}