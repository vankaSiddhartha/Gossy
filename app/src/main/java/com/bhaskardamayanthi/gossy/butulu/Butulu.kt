package com.bhaskardamayanthi.gossy.butulu

object Butulu {
   // val list = listOf<String>("Arripuku","puka","lanja","modda","Arripuka","sulli")
    fun checkText(input: String,list: ArrayList<String>): Boolean {
        for (word in list) {
            if (input.contains(word, ignoreCase = true)) {
                // Word found in the input text
                return true
            }
        }
        // No banned words found in the input text
        return false
    }

}