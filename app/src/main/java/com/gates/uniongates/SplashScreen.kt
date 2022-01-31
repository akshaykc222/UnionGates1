package com.gates.uniongates

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        }
        setContentView(R.layout.activity_splash_screen)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           // window.statusBarColor = ContextCompat.getColor(this, R.color.dark_red)
        }
        val sessionManager=SessionManager(this)
        if (sessionManager.GetLang()=="1"){
            //showLanguageAlert()
        }else{
                setLocale(sessionManager.GetLang())      //recreate()
        }
        if (sessionManager.GetPhone()=="1"){
        Handler(Looper.getMainLooper()).postDelayed({
           startActivity(Intent(this,Interduction::class.java
           ))
            finish()
        }, 8000)
        }else{
            is_account_activated()
        }
    }
    private fun is_account_activated(){
        /*only for deom*/

        // val intent:Intent
        val sessionManager=SessionManager(this);
        val phoneWithCountryCode =sessionManager.GetPhone()
        var phone=""
        if (phoneWithCountryCode.length==13){
            phone=phoneWithCountryCode.replace("+91","")
            Log.d("SplashScreen", phone)
        }

        var isActivated:Boolean=false
        Log.d("Login",phone.toString())
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        val ref= FirebaseDatabase.getInstance().getReference("Register/$phone")
        ref.keepSynced(true)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.i("Login",error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    Log.i("Login",snapshot.key.toString())

                        val json:HashMap<String,Any> = snapshot.value as HashMap<String, Any>
                    Handler(Looper.getMainLooper()).postDelayed({
                        if(!json.containsKey("Photo")){
                            val intent=Intent(this@SplashScreen,ImageUpload::class.java)
                            intent.putExtra("phone",phone)
                            startActivity(intent)
                            finish()
                        }else if (json["isApproved"]==true){
                            startActivity(Intent(this@SplashScreen,Interduction::class.java))
                            finish()
                        }else if (json["isApproved"]==false){
                            startActivity(Intent(this@SplashScreen,FailedActivation::class.java))
                            finish()
                        }

                    }, 4000)



                    /*   val gh=RegisterObject(snapshot)
                       if (gh!!){
                        startActivity(Intent(this@Login,MainActivity::class.java))
                    }else{
                        startActivity(Intent(this@Login,FailedActivation::class.java))
                    }*/
                    // Log.i("Login",h.toString())



                    //isActivated=gh.IsApproved

                }else{
                    //startActivity(Intent(this@SplashScreen,Register::class.java))
                }
            }

        })

    }
    private fun setLocale(s: String) {

       val locale = Locale(s)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)


        //  startActivity(Intent(this,Interduction::class.java))

    }
}