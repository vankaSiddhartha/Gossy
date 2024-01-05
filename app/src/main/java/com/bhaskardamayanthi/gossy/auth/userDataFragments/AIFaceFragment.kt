package com.bhaskardamayanthi.gossy.auth.userDataFragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.aiNamesAndImage.AiImage
import com.bhaskardamayanthi.gossy.aiNamesAndImage.AiNames
import com.bhaskardamayanthi.gossy.auth.FetchDataLoadingActivity
import com.bhaskardamayanthi.gossy.auth.PhoneAuthActivity
import com.bhaskardamayanthi.gossy.auth.WelcomeAccountActivity
import com.bhaskardamayanthi.gossy.databinding.FragmentAIFaceBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import retrofit2.http.Url
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
        val firstNameIndex = Random.nextInt(aiNames.firstNames.size-1)
        val secondNameIndex = Random.nextInt(aiNames.lastNames.size-1)
        val fakeName = aiNames.firstNames.get(firstNameIndex)+aiNames.lastNames.get(secondNameIndex)+newId
        saveFakeName = fakeName

        binding.fakename.text = fakeName
        val sex = storeManager.getString("sex","0")
        val aiImage = AiImage()
        if (sex.equals("male")){

            val generatedMaleImageId = Random.nextInt(aiImage.maleImg.size)
           val maleImg =  aiImage.maleImg.get(generatedMaleImageId)
            //Glide.with(requireContext()).load(maleImg).into(binding.aiFakeProfle)
            loadImageWithProgressBar(maleImg)
            saveFakeProfile = maleImg


        }else if(sex.equals("female")){
            val generatedFemaleImageId = Random.nextInt(aiImage.femaleImg.size)
            val femaleImg = aiImage.femaleImg.get(generatedFemaleImageId)
         //   Glide.with(requireContext()).load(femaleImg).into(binding.aiFakeProfle)
            loadImageWithProgressBar(femaleImg)
            saveFakeProfile = femaleImg


        } else{
            val generatedFemaleImageId = Random.nextInt(aiImage.femaleImg.size)
            val femaleImg = aiImage.femaleImg[generatedFemaleImageId]
       //     Glide.with(requireContext()).load(femaleImg).into(binding.aiFakeProfle)
            loadImageWithProgressBar(femaleImg)
            saveFakeProfile = femaleImg
        }

    }

    fun loadImageWithProgressBar(urlImage: String ){
        // Show progress bar
   //     binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.startShimmer()
        Glide.with(requireContext())
            .load(urlImage)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                   binding.shimmerFrameLayout.hideShimmer()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.shimmerFrameLayout.hideShimmer()
                    return false
                }

            })
            .into(binding.aiFakeProfle)
    }
//    override fun onLoadFailed(
//        e: GlideException?,
//        model: Any?,
//        target: kotlin.annotation.Target<Drawable>?,
//        isFirstResource: Boolean
//    ): Boolean {
//        // Hide progress bar if image fails to load
//
//        return false
//    }
//
//    override fun onResourceReady(
//        resource: Drawable?,
//        model: Any?,
//        target: kotlin.annotation.Target<Drawable>?,
//        dataSource: DataSource?,
//        isFirstResource: Boolean
//    ): Boolean {
//        // Hide progress bar when the image is loaded successfully
//
//        return false
//    }
}