package com.bhaskardamayanthi.gossy.editAccountActivities.editFragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import com.bhaskardamayanthi.gossy.MainActivity
import com.bhaskardamayanthi.gossy.aiNamesAndImage.AiNames
import com.bhaskardamayanthi.gossy.databinding.FragmentChangeFakeNameBinding
import com.bhaskardamayanthi.gossy.loading.Loading
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random


class ChangeFakeNameFragment : Fragment() {
private lateinit var binding:FragmentChangeFakeNameBinding
private lateinit var storeManager: StoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToMainActivity()
        }

        binding = FragmentChangeFakeNameBinding.inflate(layoutInflater,container,false)
        storeManager = StoreManager(requireContext())
        val number = storeManager.getString("number","")
        val fakeName = storeManager.getString("fakeName","")
        binding.fakename.text = fakeName
        binding.genrateNew.setOnClickListener {
            binding.fakename.text = genrateName()
        }
        binding.upload.setOnClickListener {
            Loading.showAlertDialogForLoading(requireContext())
            updateFakeName(number,binding.fakename.text.toString())
        }

        return binding.root
    }
    private fun genrateName(): String {
        val aiNames = AiNames()
        val newId = Random.nextInt(100).toString()
        val firstNameIndex = Random.nextInt(aiNames.firstNames.size - 1)
        val secondNameIndex = Random.nextInt(aiNames.lastNames.size - 1)
        return aiNames.firstNames.get(firstNameIndex) + aiNames.lastNames.get(secondNameIndex) + newId
    }
    private fun updateFakeName(number:String,name:String){

        val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users")
        database.child(number).child("fakeName").setValue(name).addOnSuccessListener {
            Toast.makeText(requireContext(), "Good job!!", Toast.LENGTH_SHORT).show()
            storeManager.saveString("fakeName",name)
            Loading.dismissDialogForLoading()
            navigateToMainActivity()

        }
    }
    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)

    }


}