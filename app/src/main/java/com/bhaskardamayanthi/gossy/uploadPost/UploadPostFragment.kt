package com.bhaskardamayanthi.gossy.uploadPost

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.butulu.Butulu
import com.bhaskardamayanthi.gossy.butulu.Butulu.checkText
import com.bhaskardamayanthi.gossy.databinding.FragmentUploadPostBinding
import com.bhaskardamayanthi.gossy.globalNotification.NotificationToAll
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FirebaseDataManager
import com.bhaskardamayanthi.gossy.managers.TokenManager
import com.bhaskardamayanthi.gossy.model.PostModel
import com.bhaskardamayanthi.gossy.viewModel.PostCheckViewModel
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
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
private lateinit var postCheck:PostCheckViewModel
private var list  = arrayListOf<String>("")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postCheck = ViewModelProvider(requireActivity())[PostCheckViewModel::class.java]
        binding = FragmentUploadPostBinding.inflate(layoutInflater,container,false)
        val storeManager = StoreManager(requireContext())
        val firebaseDataManager = FirebaseDataManager()
        val notificationToAll = NotificationToAll()
        val fakeName = storeManager.getString("fakeName","")
        val gender = storeManager.getString("sex","")
        val number = storeManager.getString("number","0")
        val token = TokenManager(requireContext()).getSavedToken()
        var alertText = ""
        if (gender=="female"){
            alertText="Bhutulu vodhu papa"
        }else{
            alertText = "Butulu vodhu bro"
        }
        postCheck.dataList.observe(requireActivity()){data->
            list = data
          //  Toast.makeText(requireContext(), "$list", Toast.LENGTH_SHORT).show()
        }


        binding.postBtn.setOnClickListener {

            notificationToAll.sendNotificationToAll("$fakeName new message",binding.postEt.text.toString(),"/topics/myTopic2")
            if (binding.postEt.text.toString().isNotEmpty()){
                if (checkText(binding.postEt.text.toString(),list)){
                    SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText(alertText).setContentText("Nuvu butulu matladetey bogovu").show()
                    return@setOnClickListener
                }
                val postId = UUID.randomUUID().toString()
                val path  = "post/$postId"
                val data = PostModel(binding.postEt.text.toString(),0,0,getCurrentDateTime(),postId,number,token,path)
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