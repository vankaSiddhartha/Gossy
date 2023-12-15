package com.bhaskardamayanthi.gossy.uploadPost

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.FragmentUploadPostBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FirebaseDataManager
import com.bhaskardamayanthi.gossy.model.PostModel
import com.bumptech.glide.Glide
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UploadPostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadPostFragment : Fragment() {
private lateinit var binding:FragmentUploadPostBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadPostBinding.inflate(layoutInflater,container,false)
        val storeManager = StoreManager(requireContext())
        val firebaseDataManager = FirebaseDataManager()

        val fakeImg = storeManager.getString("fakeImg","")
        val number = storeManager.getString("number","0")


        binding.postBtn.setOnClickListener {
            if (binding.postEt.text.toString().isNotEmpty()){
                val data = PostModel(binding.postEt.text.toString(),0,0,getCurrentDateTime(),UUID.randomUUID().toString(),number)
                firebaseDataManager.uploadPostToDatabase(number,data,requireContext())
            }else{
                SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("empty fields").setContentText("Please fill").show()
            }
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Customize the format as needed
        return currentDateTime.format(formatter)

    }

}