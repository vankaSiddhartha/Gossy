package com.bhaskardamayanthi.gossy.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.NotificationItemBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.GETfromFirebaseManager
import com.bhaskardamayanthi.gossy.model.NotificationModel
import com.bhaskardamayanthi.gossy.notificationSee.SeeCommentActivity
import com.bhaskardamayanthi.gossy.notificationSee.SeeLikePostActivity
import com.bhaskardamayanthi.gossy.notificationSee.SeeThePollActivity
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NotificationAdapter(val context:Context):RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    var list = mutableListOf<NotificationModel>()
    fun updateData(newList:MutableList<NotificationModel>){
        list = newList
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding:NotificationItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storeManager =StoreManager(context)
        val number = storeManager.getString("number","")
        val geTfromFirebaseManager = GETfromFirebaseManager()
        //geTfromFirebaseManager.seen(number,list[position].notificationId.toString())

        if (list[position].type=="poll"){
            if (list[position].title!!.contains("girl")){
                val pinkFlame = ContextCompat.getDrawable(context, R.drawable.girls_notification)
                holder.binding.notificationLogo.setImageDrawable(pinkFlame)
            }else if (list[position].title!!.contains("boy")){
                val blueFlame = ContextCompat.getDrawable(context, R.drawable.boys_notification)
                holder.binding.notificationLogo.setImageDrawable(blueFlame)
            }

        }

        val grey = Color.GRAY

        holder.binding.notificationText.text =list[position].title
        holder.binding.timeText.text = getRemainingTime(list[position].time.toString()).toString()
        if (   list[position].seen){
            holder.binding.notificationCard.setCardBackgroundColor(grey)
        }
        holder.binding.notificationCard.setOnClickListener {
            if (list[position].type=="poll") {
                val intent = Intent(context, SeeThePollActivity::class.java)
                intent.putExtra("question", list[position].message)
                intent.putExtra("sex", list[position].title)
                context.startActivity(intent)
            }else if (list[position].type =="like"){
                val intent = Intent(context, SeeLikePostActivity::class.java)
                intent.putExtra("path", list[position].from)
                context.startActivity(intent)
            }else{
                val  intent = Intent(context,SeeLikePostActivity::class.java)
                intent.putExtra("path",list[position].from)
                context.startActivity(intent)
            }
        }



    }
    fun getRemainingTime(dateString: String): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val currentTime = Date()

        val differenceInMillis = currentTime.time - date.time
        val seconds = differenceInMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "$days days"
            hours > 0 -> "$hours hrs"
            minutes > 0 -> "$minutes min"
            else -> "$seconds sec"
        }
    }

}