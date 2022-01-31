package com.gates.uniongates

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.loading_screen.*
import kotlinx.android.synthetic.main.otp_screen.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.suspendCoroutine


class Login : AppCompatActivity() {
   lateinit var auth:FirebaseAuth
    val TAG="LOGIN"
    private val PHONE_NUMBER_HINT = 100
    private val PERMISSION_REQ_CODE = 200
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var phoneNumberS:String=""
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
      //  is_account_activated()
    //    startActivity(Intent(this,MainActivity::class.java))
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as
                TelephonyManager
       requestPermission()
        Glide.with(this).load(R.drawable.spinner).into(loading_gif)
        loading_View.visibility=View.GONE
        val sessionManager=SessionManager(this)
        if (sessionManager.GetPhone()!="1"){
            loading_View.visibility=View.VISIBLE
            loading_View.bringToFront()
            is_account_activated(sessionManager.GetPhone().replace("+91",""))
        }
        register.setOnClickListener{
            startActivity(Intent(this, Register::class.java))
        }
        auth= FirebaseAuth.getInstance()
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                Toast.makeText(this@Login,e.toString(),Toast.LENGTH_LONG).show()
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
                visibleOtpWidget(phoneNumberS)
            }
        }
        sign.setOnClickListener {
            HideKeyboard()
            if (phone.text?.isEmpty()!!){
                Snackbar.make(it,"Enter 10 digit phone number",Snackbar.LENGTH_LONG).show()
            }else if (phone.text.toString().length!=10){
                Snackbar.make(it,"Enter valid phone number",Snackbar.LENGTH_LONG).show()
            }else{
                loading_View.visibility=View.VISIBLE
                loading_View.bringToFront()
                val Session=SessionManager(this)
               // Session.SavePhone(phone.text.toString())
               // is_account_activated(phone.text.toString())
                checkAccount(phone.text.toString())



            }
        }
    }
    fun SendVerification(phoneNumber:String){
        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information


                        val user = task.result?.user
                        val Session=SessionManager(this)
                        Session.SavePhone(user?.phoneNumber.toString())
                       // val intent:Intent
                        val phone=user?.phoneNumber.toString().replace("+91","")
                        Log.d(TAG, "signInWithCredential:success${phone.trim()}")
                        is_account_activated(phone.trim())


                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                        // Update UI
                    }
                }


    }
    private fun requestPermission(){
        val read:Int= ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val write:Int= ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=
            PackageManager.PERMISSION_GRANTED &&read!=PackageManager.PERMISSION_GRANTED && write!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE,
                READ_SMS, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQ_CODE
            )
        }
    }
    private fun HideKeyboard(){
        val imm:InputMethodManager= this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view=this.currentFocus
        if (view == null){
            view= View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }
    @SuppressLint("SetTextI18n")
    fun visibleOtpWidget(phone:String){

        otpScreen.visibility=View.VISIBLE
        otp_send_msg.text="Otp will be send to $phone"
        val countDownTimer=object : CountDownTimer(60000,1000){
            override fun onFinish() {

            }

            override fun onTick(millisUntilFinished: Long) {
                countdownt.text=""+ String.format("${TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)} : ${TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)}")
            }

        }
        countDownTimer.start()
    }

    private fun resendVerificationCode(
            phoneNumber: String,
            token: PhoneAuthProvider.ForceResendingToken?
    ) {
        Log.d(TAG,phoneNumber)
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this@Login)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
    }
      fun checkAccount(phone: String): Boolean? {
            var accountExists:Boolean?=null

        CoroutineScope(Dispatchers.IO).launch {

            val ref=FirebaseDatabase.getInstance().getReference("Register/$phone")
            ref.addListenerForSingleValueEvent(object :ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.i("Login", error.toString())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    accountExists = if (snapshot.exists()) {
                        Log.i("Login", snapshot.toString())
                        true
                    }else{
                        false
                    }
                    if (accountExists==true){
                        phoneNumberS= "+91$phone"
                        SendVerification("+91$phone")
                    }else{
                        Snackbar.make(sign,"Please register to login.",Snackbar.LENGTH_LONG).show()
                        startActivity(Intent(this@Login,Register::class.java))
                    }
                }

            })
        }
            Log.d(TAG,"waitnif")
         return accountExists
                    }

    private fun is_account_activated(phone:String){
        /*only for deom*/

        // val intent:Intent

        var isActivated:Boolean=false
        Log.d("Login",phone.toString())
       val ref=FirebaseDatabase.getInstance().getReference("Register/$phone")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.i("Login",error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    Log.i("Login",snapshot.key.toString())
                    for (h in snapshot.children){
                        if (h.key.toString()=="isApproved"){
                            Log.i("Login",h.key.toString()+h.value.toString())
                            if (h.value as Boolean){
                                startActivity(Intent(this@Login,Interduction::class.java))
                                finish()
                            }else{
                                startActivity(Intent(this@Login,FailedActivation::class.java))
                                finish()
                            }
                        }

                    }

                    /*   val gh=RegisterObject(snapshot)
                       if (gh!!){
                        startActivity(Intent(this@Login,MainActivity::class.java))
                    }else{
                        startActivity(Intent(this@Login,FailedActivation::class.java))
                    }*/
                       // Log.i("Login",h.toString())



                            //isActivated=gh.IsApproved

                }else{
                    startActivity(Intent(this@Login,Register::class.java))
                }
            }

        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_REQ_CODE ->{
                if (grantResults.size >0){
                    Log.d(TAG,grantResults.size.toString())
                    if (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED && grantResults[3]==PackageManager.PERMISSION_GRANTED){

                    } else {
                      requestPermission()
                    }
                }

            }
        }
    }
}