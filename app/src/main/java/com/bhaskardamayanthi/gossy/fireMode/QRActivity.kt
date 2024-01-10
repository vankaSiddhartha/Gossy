package com.bhaskardamayanthi.gossy.fireMode

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivityQractivityBinding
import com.bhaskardamayanthi.gossy.loading.Loading
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.values
import com.google.firebase.ktx.Firebase

class QRActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQractivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQractivityBinding.inflate(layoutInflater)
        Loading.showAlertDialogForLoading(this)
        val storeManager = StoreManager(this)
        val name = storeManager.getString("name1","")
        val  name2 = storeManager.getString("name2","")
        binding.text.text = "Take screenshot of qr code and pay in Google pay or phone pay or paytm"
        getQr { img->
            Glide.with(this)
                .load(img)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Loading.dismissDialogForLoading()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        Loading.dismissDialogForLoading()
                        return false
                    }

                })
                .into(binding.qr)

        }
        setContentView(binding.root)
    }

    fun getQr(callback: (String) -> Unit) {
        val ref =
            Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(
                "qr"
            )
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val qr = snapshot.value.toString()
                callback(qr) // Pass the retrieved QR value to the callback function
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
            }
        })
    }
}