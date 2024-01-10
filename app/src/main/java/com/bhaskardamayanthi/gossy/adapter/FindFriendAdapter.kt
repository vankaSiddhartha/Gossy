package com.bhaskardamayanthi.gossy.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.FragmentFindFriendBinding
import com.bhaskardamayanthi.gossy.databinding.UserSearchItemLayoutBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FirebaseDataManager
import com.bhaskardamayanthi.gossy.managers.FragmentIntentManager.intentFragment
import com.bhaskardamayanthi.gossy.managers.GETfromFirebaseManager
import com.bhaskardamayanthi.gossy.managers.NameToProfileManager.getProfileInitial
import com.bhaskardamayanthi.gossy.managers.NameToProfileManager.getRandomColor
import com.bhaskardamayanthi.gossy.managers.NameToProfileManager.textAsBitmap
import com.bhaskardamayanthi.gossy.model.Contact
import com.bhaskardamayanthi.gossy.model.UserModel
import com.bhaskardamayanthi.gossy.polls.TagBottomFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.userProfileChangeRequest
import java.util.Random
import kotlin.properties.Delegates

class FindFriendAdapter(val context:Context):RecyclerView.Adapter<FindFriendAdapter.ViewHolder>() {
    var list = mutableListOf<UserModel>()
    var contactsList = emptyList<Contact>()
    var friendsList = listOf<UserModel>()

    fun updateData(data: MutableList<UserModel>){
        list = data
        notifyDataSetChanged()
    }
    fun  getFriends(data:List<UserModel>){
        friendsList= data
    }
    fun removeItem(position: Int) {
     // list.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }
    fun updateContacts(data:List<Contact>){
        contactsList = data
    }

    inner  class ViewHolder(val binding:UserSearchItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(UserSearchItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
      return friendsList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val firebaseDataManager = FirebaseDataManager()
        val geTfromFirebaseManager = GETfromFirebaseManager()
        val storeManager = StoreManager(context)
        val userName = storeManager.getString("fakeName", "")
        val number = storeManager.getString("number", "")
        geTfromFirebaseManager.checkIfFriend(number, friendsList[position].phone.toString()) { value ->
            if (value) {
                holder.binding.userAddSearch.visibility = View.INVISIBLE

            }
              if (friendsList[position].profile.toString().isEmpty()){
                val initial = getProfileInitial(friendsList[position].name.toString())


                holder.binding.userNameSearch.text = friendsList[position].name
                val textColor = Color.GRAY // Text color
                val backgroundColor = getRandomColor() // Random background color
                val bitmap = textAsBitmap(initial, 20f, textColor, backgroundColor)
                holder.binding.userProfileSearch.setImageBitmap(bitmap)
            }else{
                holder.binding.userNameSearch.text = friendsList[position].name
                Glide.with(context).load(friendsList[position].profile).into(holder.binding.userProfileSearch)
            }

            holder.binding.userAddSearch.setOnClickListener {
                firebaseDataManager.addFriends(number, friendsList[position].phone.toString(), context)
                removeItem(position)

            }


        }
    }
    }


