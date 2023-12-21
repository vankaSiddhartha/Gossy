package com.bhaskardamayanthi.gossy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bhaskardamayanthi.gossy.account.AccountFragment
import com.bhaskardamayanthi.gossy.anonymousPost.AnonymousPostFragment
import com.bhaskardamayanthi.gossy.databinding.ActivityMainBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FragmentIntentManager.intentFragment
import com.bhaskardamayanthi.gossy.notificationFragments.NotificationFragment
import com.bhaskardamayanthi.gossy.notificationSee.SeeThePollActivity
import com.bhaskardamayanthi.gossy.polls.FindFriendFragment
import com.bhaskardamayanthi.gossy.polls.PollFragment
import com.bhaskardamayanthi.gossy.trending.TrendingFragment
import com.bhaskardamayanthi.gossy.uploadPost.UploadPostFragment
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
   private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
      //  startActivity(Intent(this,SeeThePollActivity::class.java))
        val storeManager = StoreManager(this)
        intentFragment(R.id.frag,AnonymousPostFragment(),this,"AnonymousPostFrag")
        binding.titleText.text = "Gossips"
        binding.searchBtn.setOnClickListener {
            intentFragment(R.id.frag,FindFriendFragment(),this@MainActivity,"SearchFragment")
        }
        binding.notificationBtn.setOnClickListener {
            intentFragment(R.id.frag,NotificationFragment(),this,"NotificationFragment")
        }
        val fakeImg = storeManager.getString("fakeImg","")
        Glide.with(this).load(fakeImg).into(binding.profile)
        binding.bottomNavigationView.setOnItemSelectedListener{menuItem->
            when (menuItem.itemId) {
               R.id.home->{
                   intentFragment(R.id.frag,AnonymousPostFragment(),this,"AnonymousPostFrag")
                   binding.titleText.text = "Gossips"
                   true
               }
                R.id.uploadPost->{
                    intentFragment(R.id.frag,UploadPostFragment(),this,"UploadPostFrag")
                    binding.titleText.text = "Upload post"
                    true
                }
                R.id.polls->{
                    binding.titleText.text = "Polls"
                    intentFragment(R.id.frag,PollFragment(),this,"PollsFragment")
                    true
                }
             R.id.trending->{
                 binding.titleText.text= "Trending posts"
                 intentFragment(R.id.frag,TrendingFragment(),this,"TrendingFragment")
                 true
             }
                R.id.account->{
                    binding.titleText.text= "Edit Account"
                    intentFragment(R.id.frag,AccountFragment(),this,"TrendingFragment")
                    true
                }
                else->{
                    true
                }
            }

        }
    }
}