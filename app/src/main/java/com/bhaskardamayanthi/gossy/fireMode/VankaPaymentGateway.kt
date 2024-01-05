package com.bhaskardamayanthi.gossy.fireMode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivityVankaPaymentGatewayBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import dev.shreyaspatil.easyupipayment.model.TransactionDetails
import dev.shreyaspatil.easyupipayment.model.TransactionStatus

class VankaPaymentGateway : AppCompatActivity(), PaymentStatusListener {
    private lateinit var binding:ActivityVankaPaymentGatewayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVankaPaymentGatewayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val storeManager  = StoreManager(this)
        val number = storeManager.getString("number","")
        val name1 = storeManager.getString("name1","")
        val name2 = storeManager.getString("name2","")


        val des = "Gossy+ user ${name1} ${name2}  ${number}"
        binding.amazonCard.setOnClickListener {
            pay(des,PaymentApp.AMAZON_PAY)
        }
        binding.bhimCard.setOnClickListener {
           pay(des,PaymentApp.BHIM_UPI)
        }
        binding.gpayCard.setOnClickListener {
            pay(des,PaymentApp.GOOGLE_PAY)
        }
        binding.paytmCard.setOnClickListener {
            pay(des,PaymentApp.PAYTM)
        }
        binding.phonePayCard.setOnClickListener {
            pay(des,PaymentApp.PHONE_PE)
        }

    }
    private fun pay(des:String,paymentApp: PaymentApp){
        try {

            val transactionId = "TID" + System.currentTimeMillis()
         val   easyUpiPayment = EasyUpiPayment(this) {
                this.paymentApp = paymentApp
                this.payeeVpa = "70758258571@ybl"
                this.payeeName = "Boppe Taruna Phaneendra"
                this.transactionId = transactionId
                this.transactionRefId = transactionId
                this.payeeMerchantCode = "70758258571@ybl"
                this.description = des
                this.amount = 1.0.toString()
            }

            easyUpiPayment.setPaymentStatusListener(this)


            easyUpiPayment.startPayment()
        } catch (e: Exception) {
            e.printStackTrace()
            toast("Error: ${e.message}")
        }
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString())


        when (transactionDetails.transactionStatus) {
            TransactionStatus.SUCCESS -> onTransactionSuccess()
            TransactionStatus.FAILURE -> onTransactionFailed()
            TransactionStatus.SUBMITTED -> onTransactionSubmitted()
        }
    }

    override fun onTransactionCancelled() {
        // Payment Cancelled by User
        toast("Cancelled by user")

    }

    private fun onTransactionSuccess() {
        // Payment Success
        toast("Success")

    }

    private fun onTransactionSubmitted() {
        // Payment Pending
        toast("Pending | Submitted")

    }

    private fun onTransactionFailed() {
        // Payment Failed
        toast("Failed")

    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}