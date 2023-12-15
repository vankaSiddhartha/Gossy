package com.bhaskardamayanthi.gossy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bhaskardamayanthi.gossy.MainActivity
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.auth.userDataFragments.AIFaceFragment
import com.bhaskardamayanthi.gossy.databinding.BranchSingleItemLayoutBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FragmentIntentManager.intentFragment
import com.bhaskardamayanthi.gossy.model.CollageModel


class ChooseCollageAdapter(val context: Context, private val list:ArrayList<CollageModel>):RecyclerView.Adapter<ChooseCollageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:BranchSingleItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
     return ViewHolder(BranchSingleItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textViewCollage.text = list[position].college
        val storeManager = StoreManager(context)
        holder.binding.collegeView.setOnClickListener {
            storeManager.saveString("college",list[position].college.toString())
            intentFragment(R.id.fragment_container,AIFaceFragment(),context,"AiFaceFrag")


        }


    }

    fun updateData(newList: List<CollageModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}