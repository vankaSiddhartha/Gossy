package com.bhaskardamayanthi.gossy.editAccountActivities.editFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import com.bhaskardamayanthi.gossy.MainActivity
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.FragmentChangeNameFragentBinding
import com.bhaskardamayanthi.gossy.loading.Loading
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ChangeNameFragment : Fragment() {

private lateinit var binding:FragmentChangeNameFragentBinding
private lateinit var storeManager: StoreManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeNameFragentBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
       storeManager = StoreManager(requireContext())

        binding.upload.visibility = View.INVISIBLE
        val number = storeManager.getString("number","")
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToMainActivity()
        }


        binding.changeNameEt.addTextChangedListener { editable ->
            if (!editable.isNullOrEmpty()) {
                binding.upload.visibility = View.VISIBLE
            } else {
                binding.upload.visibility = View.INVISIBLE
            }
        }
        binding.upload.setOnClickListener {
            Loading.showAlertDialogForLoading(requireContext())
            updateName(number,binding.changeNameEt.text.toString())
        }
        return binding.root

    }
    private fun updateName(number:String,name:String){

        val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users")
        database.child(number).child("name").setValue(name).addOnSuccessListener {
            Toast.makeText(requireContext(), "Name is updated!!", Toast.LENGTH_SHORT).show()
            storeManager.saveString("name",name)
            Loading.dismissDialogForLoading()

        }
    }
    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)

    }

}