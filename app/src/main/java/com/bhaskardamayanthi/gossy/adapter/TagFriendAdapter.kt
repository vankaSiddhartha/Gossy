package com.bhaskardamayanthi.gossy.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bhaskardamayanthi.gossy.databinding.TagFriendItemBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FirebaseDataManager
import com.bhaskardamayanthi.gossy.managers.NameToProfileManager
import com.bhaskardamayanthi.gossy.model.FriendsModel
import com.bhaskardamayanthi.gossy.model.QuestionModel
import com.bhaskardamayanthi.gossy.model.UserModel

class TagFriendAdapter(val context: Context):RecyclerView.Adapter<TagFriendAdapter.ViewModel>() {
    var list = mutableListOf<UserModel>()
    var question = ""
    fun addNewData(data:MutableList<UserModel>){
        list = data
        notifyDataSetChanged()
    }
    fun getQuestion(nQuestion:String){
        question = nQuestion
    }
    inner class  ViewModel(val binding:TagFriendItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        return ViewModel(TagFriendItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        val firebaseDataManager = FirebaseDataManager()
        val storeManager = StoreManager(context)
        val userId = storeManager.getString("number","")
        val gender = storeManager.getString("sex","")
        val initial = NameToProfileManager.getProfileInitial(list[position].name.toString())
       // Toast.makeText(context, list[position].toString(), Toast.LENGTH_SHORT).show()
        holder.binding.userNameSearch.text = list[position].name
        val textColor = Color.GRAY // Text color
       val backgroundColor = NameToProfileManager.getRandomColor() // Random background color

       val bitmap = NameToProfileManager.textAsBitmap(initial, 20f, textColor, backgroundColor)
       holder.binding.userProfileSearch.setImageBitmap(bitmap)
        holder.binding.tag.setOnClickListener{
            Toast.makeText(context, "sending to... "+list[position].name, Toast.LENGTH_SHORT).show()
            if (gender=="male") {
                firebaseDataManager.sendPollAndLikeAndPostNotifications(
                    userId,
                    list[position].phone.toString(),
                    "poll",
                    "From a boy!!",
                    question,
                    list[position].fcmToken.toString()
                )
            }else if (gender=="female"){
                firebaseDataManager.sendPollAndLikeAndPostNotifications(
                    userId,
                    list[position].phone.toString(),
                    "poll",
                    "From a girl!!",
                    question,
                    list[position].fcmToken.toString()
                )
            }else{
                firebaseDataManager.sendPollAndLikeAndPostNotifications(
                    userId,
                    list[position].phone.toString(),
                    "poll",
                    "some one flamed you !!",
                    question,
                    list[position].fcmToken.toString()
                )
            }
        }
    }

    private fun showUser(position: Int) {

    }
}