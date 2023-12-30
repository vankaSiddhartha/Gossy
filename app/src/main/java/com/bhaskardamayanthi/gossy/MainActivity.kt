package com.bhaskardamayanthi.gossy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bhaskardamayanthi.gossy.account.AccountFragment
import com.bhaskardamayanthi.gossy.anonymousPost.AnonymousPostFragment
import com.bhaskardamayanthi.gossy.databinding.ActivityMainBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FragmentIntentManager.intentFragment
import com.bhaskardamayanthi.gossy.managers.GETfromFirebaseManager
import com.bhaskardamayanthi.gossy.managers.TokenManager
import com.bhaskardamayanthi.gossy.model.NotificationModel
import com.bhaskardamayanthi.gossy.notificationFragments.NotificationFragment
import com.bhaskardamayanthi.gossy.notificationSee.SeeThePollActivity
import com.bhaskardamayanthi.gossy.polls.FindFriendFragment
import com.bhaskardamayanthi.gossy.polls.PollFragment
import com.bhaskardamayanthi.gossy.settings.SettingsFragment
import com.bhaskardamayanthi.gossy.trending.TrendingFragment
import com.bhaskardamayanthi.gossy.uploadPost.UploadPostFragment
import com.bhaskardamayanthi.gossy.viewModel.NotificationViewModel
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
   private lateinit var binding:ActivityMainBinding
    private lateinit var viewModel: NotificationViewModel

    companion object {
        @JvmStatic
        private var notiList: MutableList<NotificationModel> = mutableListOf()

        fun getNotiList(): MutableList<NotificationModel> {
            return notiList
        }
        fun updateNotiList(newList: MutableList<NotificationModel>) {
            notiList.clear()
            notiList.addAll(newList)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        notiList = mutableListOf()

        val storeManager = StoreManager(this)
        val number = storeManager.getString("number","")
        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        viewModel.fetchData(number)
        supportActionBar?.hide()
//        val fargintent = intent.getStringExtra("key")
//        if (fargintent=="noti"){
//            intentFragment(R.id.frag,NotificationFragment(),this,"NotificationFragment")
//        }
        viewModel.liveData.observe(this){newData->
            updateNotiList(newData)
           //

        }
        viewModel.countValue.observe(this){notificationCount->
            if (notificationCount>0){
                binding.notificationBadge.visibility = View.VISIBLE
                binding.notificationBadge.text = notificationCount.toString()
            }
            else{
                binding.notificationBadge.visibility = View.INVISIBLE
            }

        }
        //val tokenManager = TokenManager(this)


      //  startActivity(Intent(this,SeeThePollActivity::class.java))



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
                R.id.settings->{
                    binding.titleText.text= "Settings"
                    intentFragment(R.id.frag,SettingsFragment(),this,"SettingFragment")
                    true
                }
                else->{
                    true
                }
            }

        }
    }




}