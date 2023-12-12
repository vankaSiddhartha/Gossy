package com.bhaskardamayanthi.gossy.auth.userDataFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.FragmentDOBYearPickerBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FragmentIntentManager.intentFragment
import java.util.Calendar


class DOBYearPickerFragment : Fragment() {

private lateinit var binding:FragmentDOBYearPickerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDOBYearPickerBinding.inflate(layoutInflater,container,false)
        // Set the range of years (adjust these values as per your requirement)
        val storeManager = StoreManager(requireContext())
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val yearPicker = binding.yearPicker
        val minYear = currentYear - 100 // set minimum year
        val maxYear = currentYear + 100 // set maximum year

        yearPicker.minValue = minYear
        yearPicker.maxValue = maxYear

// Calculate the year 18 years ago
        val year18YearsAgo = currentYear - 18

// Set the year picker value to the year 18 years ago
        yearPicker.value = year18YearsAgo
binding.upload.setOnClickListener {
    storeManager.saveString("dobYear",yearPicker.value.toString())
    navigateToNextFragment(GenderChooseFragment())
}
binding.back.setOnClickListener {
    intentFragment(R.id.fragment_container,NameFragment(),requireContext())
}

        return binding.root
    }
    fun navigateToNextFragment(nextFragment:Fragment) {
        // Replace NextFragment with the actual name of your next fragment

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, nextFragment) // R.id.fragment_container is the ID of the layout container for fragments
        transaction.addToBackStack(null) // Optional: Add transaction to back stack to enable back navigation
        transaction.commit()
    }

}