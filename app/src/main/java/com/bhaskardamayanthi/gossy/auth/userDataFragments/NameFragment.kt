package com.bhaskardamayanthi.gossy.auth.userDataFragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.auth.WelcomeAccountActivity
import com.bhaskardamayanthi.gossy.databinding.FragmentNameBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager


class NameFragment : Fragment() {
private lateinit var binding:FragmentNameBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding= FragmentNameBinding.inflate(layoutInflater,container,false)


        binding.upload.setOnClickListener {


            onClick()
        }

        binding.name2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used, but required by TextWatcher interface
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used, but required by TextWatcher interface
            }

            override fun afterTextChanged(s: Editable?) {
                val name = s?.toString() ?: ""
                if (name.isNotEmpty()) {

                    binding.upload.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.btnBlue))
                } else {
                    // If text is empty, you can reset the card view color to default here
                    binding.upload.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))

                }
            }
        })
        binding.back.setOnClickListener {
            startActivity(Intent(requireContext(), WelcomeAccountActivity::class.java))
        }

        return binding.root

    }
    private fun onClick(){
        val storeManager = StoreManager(requireContext())
        val name = binding.name.text.toString()
        val secondName = binding.name2.text.toString()
        if (name.isNotEmpty()&&secondName.isNotEmpty())
        {
            storeManager.saveString("name",name)
            storeManager.saveString("name1",binding.name.text.toString())
            storeManager.saveString("name2",binding.name2.text.toString())
            navigateToNextFragment(DOBYearPickerFragment())

        }else{
            SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
                .setContentText("Fill the name").show()
        }

    }
    fun navigateToNextFragment(nextFragment:Fragment) {
         // Replace NextFragment with the actual name of your next fragment

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, nextFragment) // R.id.fragment_container is the ID of the layout container for fragments
        transaction.addToBackStack(null) // Optional: Add transaction to back stack to enable back navigation
        transaction.commit()
    }


}