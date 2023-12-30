package com.bhaskardamayanthi.gossy.editAccountActivities.editFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bhaskardamayanthi.gossy.MainActivity
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.aiNamesAndImage.AiImage
import com.bhaskardamayanthi.gossy.databinding.FragmentChangeFakeProfileBinding
import com.bhaskardamayanthi.gossy.loading.Loading
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bumptech.glide.Glide
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random


class ChangeFakeProfileFragment : Fragment() {
private lateinit var binding:FragmentChangeFakeProfileBinding
private lateinit var saveFakeProfile:String
private lateinit var storeManager:StoreManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToMainActivity()
        }

        binding = FragmentChangeFakeProfileBinding.inflate(layoutInflater,container,false)
        storeManager = StoreManager(requireContext())
        val img = storeManager.getString("fakeImg","")
        saveFakeProfile = img
        Glide.with(requireContext()).load(saveFakeProfile).load(img).into(binding.circleImageView)
        val aiImages  = AiImage()
        val sex = storeManager.getString("sex","0")
        val phone = storeManager.getString("number","")
        binding.genrateNew.setOnClickListener {
            genrateNewImg(sex,aiImages)
        }
        binding.upload.setOnClickListener {
            Loading.showAlertDialogForLoading(requireContext())
           updateFakeProfile(phone,saveFakeProfile)
        }
        return binding.root
    }
 private fun genrateNewImg(sex:String,aiImage:AiImage){
     if (sex.equals("male")){

         val generatedMaleImageId = Random.nextInt(aiImage.maleImg.size)
         val maleImg =  aiImage.maleImg.get(generatedMaleImageId)
         Glide.with(requireContext()).load(maleImg).into(binding.circleImageView)
         saveFakeProfile = maleImg


     }else if(sex.equals("female")){
         val generatedFemaleImageId = Random.nextInt(aiImage.femaleImg.size)
         val femaleImg = aiImage.femaleImg.get(generatedFemaleImageId)
         Glide.with(requireContext()).load(femaleImg).into(binding.circleImageView)
         saveFakeProfile = femaleImg


     } else{
         val generatedFemaleImageId = Random.nextInt(aiImage.femaleImg.size)
         val femaleImg = aiImage.femaleImg[generatedFemaleImageId]
         Glide.with(requireContext()).load(femaleImg).into(binding.circleImageView)
         saveFakeProfile = femaleImg
     }
 }
    private fun updateFakeProfile(number:String,name:String){

        val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users")
        database.child(number).child("fakeImg").setValue(name).addOnSuccessListener {
            Toast.makeText(requireContext(), "Good job!!", Toast.LENGTH_SHORT).show()
            storeManager.saveString("fakeImg",name)
            Loading.dismissDialogForLoading()
           navigateToMainActivity()
        }
    }
    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)

    }
}