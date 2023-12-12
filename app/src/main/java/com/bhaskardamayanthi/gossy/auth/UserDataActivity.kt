package com.bhaskardamayanthi.gossy.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.auth.userDataFragments.AIFaceFragment
import com.bhaskardamayanthi.gossy.auth.userDataFragments.NameFragment
import com.bhaskardamayanthi.gossy.auth.userDataFragments.SelectCollegeFragment
import com.bhaskardamayanthi.gossy.databinding.ActivityUserDataBinding

class UserDataActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserDataBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
            // Begin a FragmentTransaction
            val fragmentManager = supportFragmentManager // Use activity's supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

// Replace or add a fragment to the FrameLayout container
        val fragment = NameFragment()// YourFragment is the class name of your fragment
        fragmentTransaction.replace(R.id.fragment_container, fragment)
// Or use add() instead of replace() if you want to add multiple fragments

// Commit the transaction
        fragmentTransaction.commit()

    }
}