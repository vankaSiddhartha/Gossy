package com.bhaskardamayanthi.gossy.managers

import android.os.CountDownTimer

class OTPTimerManager(private val listener: OnTimerTickListener) : CountDownTimer(60000, 1000) {

    interface OnTimerTickListener {
        fun onTick(secondsRemaining: Long)
        fun onFinish()
    }

    override fun onTick(millisUntilFinished: Long) {
        val secondsRemaining = millisUntilFinished / 1000
        listener.onTick(secondsRemaining)
    }

    override fun onFinish() {
        listener.onFinish()
    }
}
