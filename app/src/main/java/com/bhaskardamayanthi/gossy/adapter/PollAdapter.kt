package com.bhaskardamayanthi.gossy.adapter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bhaskardamayanthi.gossy.databinding.PollItemBinding
import com.bhaskardamayanthi.gossy.managers.EmojiManager.emojiList
import com.bhaskardamayanthi.gossy.managers.EmojiManager.getEmoji
import com.bhaskardamayanthi.gossy.managers.PollsFragColorsManager.backgroundColors
import com.bhaskardamayanthi.gossy.managers.PollsFragColorsManager.textColors
import com.bhaskardamayanthi.gossy.model.CollageModel
import com.bhaskardamayanthi.gossy.model.QuestionModel
import com.bhaskardamayanthi.gossy.polls.TagBottomFragment
import kotlin.random.Random

class PollAdapter(val context: Context):RecyclerView.Adapter<PollAdapter.ViewHolder>() {
    var list = mutableListOf<QuestionModel>()
    fun updateList(newData:MutableList<QuestionModel>){
        list = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding:PollItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PollItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        if (list.size==0){
            return 0
        }else{
            return  list[0].questions.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val random = Random(System.currentTimeMillis())



        val randomNumber = random.nextInt(0, emojiList.size)
        val emoji= getEmoji(randomNumber)
        val rondomColors = random.nextInt(0, backgroundColors.size)
       // Toast.makeText(context, backgroundColors.size.toString(), Toast.LENGTH_SHORT).show()
        val parsedColor = Color.parseColor(backgroundColors[rondomColors])
        val textColor =Color.parseColor(textColors[rondomColors])
        holder.binding.questionId.setTextColor(textColor)
        holder.binding.pollItem.setBackgroundColor(parsedColor)
        holder.binding.emojiId.text = emoji
        holder.binding.questionNo.text = "${position+1} of ${list[0].questions.size}"
        holder.binding.questionId.text = list[0].questions[position]
        holder.binding.tagBtn.setOnClickListener{
            val tagBottomFragment = TagBottomFragment()
            val bundle = Bundle().apply {
                putString("question",list[0].questions[position]) // Replace "key" with your key and provide the data
            }
            tagBottomFragment.arguments = bundle
            tagBottomFragment.show((context as AppCompatActivity).supportFragmentManager, tagBottomFragment.tag)
        }
    }
}