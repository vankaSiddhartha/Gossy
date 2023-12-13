package com.bhaskardamayanthi.gossy.auth.userDataFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.aiNamesAndImage.AiImage
import com.bhaskardamayanthi.gossy.aiNamesAndImage.AiNames
import com.bhaskardamayanthi.gossy.auth.FetchDataLoadingActivity
import com.bhaskardamayanthi.gossy.auth.PhoneAuthActivity
import com.bhaskardamayanthi.gossy.auth.WelcomeAccountActivity
import com.bhaskardamayanthi.gossy.databinding.FragmentAIFaceBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bumptech.glide.Glide
import kotlin.random.Random


class AIFaceFragment : Fragment() {
private lateinit var binding:FragmentAIFaceBinding
private lateinit var saveFakeName:String
private lateinit var saveFakeProfile:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAIFaceBinding.inflate(layoutInflater,container,false)
        val storeManager = StoreManager(requireContext())
        saveFakeName =""
        saveFakeProfile =""
        generatedAIImageAndName()
        binding.generateNew.setOnClickListener {
            generatedAIImageAndName()
        }
        binding.upload.setOnClickListener {
            storeManager.saveString("fakeName",saveFakeName)
            storeManager.saveString("fakeImg",saveFakeProfile)
            startActivity(Intent(requireContext(),FetchDataLoadingActivity::class.java))
        }
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }
    private fun generatedAIImageAndName(){
        val aiNames = AiNames()

        val storeManager = StoreManager(requireContext())
        val newId = Random.nextInt(100).toString()
        val firstNameIndex = Random.nextInt(50)
        val secondNameIndex = Random.nextInt(50)
        val fakeName = aiNames.firstNames.get(firstNameIndex)+aiNames.lastNames.get(secondNameIndex)+newId
        saveFakeName = fakeName

        binding.fakename.text = fakeName
        val sex = storeManager.getString("sex","0")
        val aiImage = AiImage()
        if (sex.equals("male")){

            val generatedMaleImageId = Random.nextInt(aiImage.maleImg.size)
           val maleImg =  aiImage.maleImg.get(generatedMaleImageId)
            Glide.with(requireContext()).load(maleImg).into(binding.aiFakeProfle)
            saveFakeProfile = maleImg


        }else if(sex.equals("female")){
            val generatedFemaleImageId = Random.nextInt(aiImage.femaleImg.size)
            val femaleImg = aiImage.femaleImg.get(generatedFemaleImageId)
            Glide.with(requireContext()).load(femaleImg).into(binding.aiFakeProfle)
            saveFakeProfile = femaleImg


        } else{
            val generatedFemaleImageId = Random.nextInt(aiImage.femaleImg.size)
            val femaleImg = aiImage.femaleImg[generatedFemaleImageId]
            Glide.with(requireContext()).load(femaleImg).into(binding.aiFakeProfle)
            saveFakeProfile = femaleImg
        }

    }


}