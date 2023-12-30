package com.bhaskardamayanthi.gossy.managers
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bhaskardamayanthi.gossy.R


object FragmentIntentManager {

    fun intentFragment(id:Int, fragment:Fragment, context:Context,tag:String){
//        val load = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
//        load.replace(id,fragment)
//        load.commit()

        val transaction: FragmentTransaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.replace(id, fragment, tag)
        transaction.addToBackStack(null)
        transaction.commit()

    }




}