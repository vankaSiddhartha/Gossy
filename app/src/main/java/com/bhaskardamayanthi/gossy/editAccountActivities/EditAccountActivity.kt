package com.bhaskardamayanthi.gossy.editAccountActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bhaskardamayanthi.gossy.MainActivity
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivityEditAccountBinding
import com.bhaskardamayanthi.gossy.editAccountActivities.editFragments.ChangeFakeNameFragment
import com.bhaskardamayanthi.gossy.editAccountActivities.editFragments.ChangeFakeProfileFragment
import com.bhaskardamayanthi.gossy.editAccountActivities.editFragments.ChangeNameFragment
import com.bhaskardamayanthi.gossy.editAccountActivities.editFragments.ChangeProfileFragment
import com.bhaskardamayanthi.gossy.managers.FragmentIntentManager.intentFragment

class EditAccountActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEditAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val key = intent.getStringExtra("key")
        binding.headText.text = key
        if (key=="Profile"){
            intentFragment(
                R.id.edit_container,ChangeProfileFragment(),this,"ChangeProfileFragment"
            )
        }
        if (key=="Name"){
            intentFragment(
                R.id.edit_container,ChangeNameFragment(),this,"ChangeNameFragment"
            )
        }
        if (key=="Gossy Name"){
            intentFragment(
                R.id.edit_container,ChangeFakeNameFragment(),this,"ChangeFakeNameFragment"
            )
        }
        if (key=="Gossy Profile"){
            intentFragment(
                R.id.edit_container, ChangeFakeProfileFragment(),this,"ChangeFakeProfileFragment"
            )
        }
        binding.back.setOnClickListener {
            navigateToMainActivity()
        }
        binding.headText.setOnClickListener {
            navigateToMainActivity()
        }
    }
    private fun navigateToMainActivity() {
       finish() 

    }
}