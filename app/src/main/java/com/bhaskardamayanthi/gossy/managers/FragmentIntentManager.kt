package com.bhaskardamayanthi.gossy.managers

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bhaskardamayanthi.gossy.R
import com.google.android.gms.dynamic.SupportFragmentWrapper

object FragmentIntentManager {

    fun intentFragment(id:Int, fragment:Fragment, context:Context){
        val load = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        load.replace(id,fragment)
        load.commit()
    }




}