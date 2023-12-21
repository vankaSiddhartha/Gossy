package com.bhaskardamayanthi.gossy.managers

import android.app.Activity
import android.content.Context
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bhaskardamayanthi.gossy.loading.Loading.dismissDialogForLoading
import com.bhaskardamayanthi.gossy.loading.Loading.showAlertDialogForLoading
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneAuthManager(private val context: Context, private val otpManager: OTPTimerManager) {

    private var verificationId: String = ""
   val  mAuth = FirebaseAuth.getInstance()
    // mAuth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)


    fun sendVerificationCode(phoneNumber: String, activity: Activity) {
// set this to remove reCaptcha web

        showAlertDialogForLoading(context)
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    // Automatically handle verification if the device is able to receive SMS
                    signInWithPhoneAuthCredential(phoneAuthCredential,phoneNumber)

                }

                override fun onVerificationFailed(e: FirebaseException) {

                    dismissDialogForLoading()
                    SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
                        .setContentText( e.message).show()
                }

                override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(s, forceResendingToken)
                    // Store verification ID to use later
                    verificationId = s
                    Toast.makeText(activity, "OTP Sent", Toast.LENGTH_SHORT).show()

                    otpManager.start()
                    dismissDialogForLoading()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    fun verifyOTP(otp: String,phoneNumber: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        signInWithPhoneAuthCredential(credential,phoneNumber)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,phoneNumber: String) {
        val storeManager = StoreManager(context)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                   // Toast.makeText(context, "Authentication Successful", Toast.LENGTH_SHORT).show()
                    storeManager.saveString("number",phoneNumber.toString())
                    SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Great")
                        .setContentText( "Authentication Successful").show()
                } else {
                    // If sign in fails, display a message to the user.
                    SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
                        .setContentText( "Authentication Failed").show()
                   // Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }


}
