package com.bhaskardamayanthi.gossy.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhaskardamayanthi.gossy.MainActivity
import com.bhaskardamayanthi.gossy.databinding.SettingItemBinding

class SettingsAdapters(
  val settingsData:List<Pair<String,String>>,
    val context: Context

):RecyclerView.Adapter<SettingsAdapters.ViewHolder>() {




    class ViewHolder(var binding:SettingItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SettingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val lightGray = Color.parseColor("#ECEFF1")
        val purpleColor = Color.parseColor("#9c27b0")
        val (currentKey, currentValue) = settingsData[position]

        if (currentKey == "My Account" || currentKey == "Support" || currentKey == "Company" || currentKey == "Follow Us" || currentKey == "Premium Mode") {
            // Handle specific keys with different UI
            holder.binding.settingTvValue.visibility = View.GONE
            holder.binding.settingsNameTv.text = currentKey
            holder.binding.settingsNameTv.setTextColor(purpleColor)
            holder.binding.settingsCard.setCardBackgroundColor(lightGray)
        } else if (!(currentKey.contains("Name"))) {
            // Hide value and set only the key
            holder.binding.settingTvValue.visibility = View.GONE
            holder.binding.settingsNameTv.text = currentKey
            holder.binding.settingsNameTv.setTextColor(Color.BLACK) // Set default text color
            holder.binding.settingsCard.setCardBackgroundColor(Color.WHITE) // Set default card color
        } else {
            // For other cases, show both key and value
            holder.binding.settingsNameTv.text = currentKey
            holder.binding.settingTvValue.text = currentValue
            holder.binding.settingTvValue.visibility = View.VISIBLE // Ensure value is visible
            holder.binding.settingsNameTv.setTextColor(Color.BLACK) // Reset text color
            holder.binding.settingsCard.setCardBackgroundColor(Color.WHITE) // Reset card color
        }






    }


    override fun getItemCount(): Int {
        return  settingsData.size
    }
}