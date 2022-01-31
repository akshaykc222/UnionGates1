package com.gates.uniongates

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_interduction.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.*


class Interduction : AppCompatActivity() {
    lateinit var locale: Locale
    private var currentLanguage = "en"
    private val STORAGE_PERMISSION_CODE = 101
    var PermissionAray:ArrayList<String> = arrayListOf(
    )
   lateinit var sessionManager:SessionManager
    var flag=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interduction)
//this code for delete
      //  SendEmail()
        val apiClient=ApiClient()
        val retrofitAPI= apiClient.getClient()?.create(ApiInterface::class.java)
        val call= retrofitAPI?.createEmail("akshay")
        call?.enqueue(object: retrofit2.Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.d("Interduction",t.toString())
            }

            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.d("Interduction", response?.body().toString())
            }

        } )

        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
        sessionManager= SessionManager(this)

        if(sessionManager.GetLang() == "ml"){
            Glide.with(this).load(ContextCompat.getDrawable(this,R.drawable.banner_ml)).into(banner)
        }else{
            Glide.with(this).load(ContextCompat.getDrawable(this,R.drawable.banner_eng)).into(banner)
        }

       /* if (sessionManager.GetLang()=="1"){
            showLanguageAlert()
        }else{
            Log.d("tag", flag.toString())

                setLocale(sessionManager.GetLang(),1)

            //recreate()
        }*/
        about_company.setOnClickListener {
            it.animate().scaleYBy(0.1f).scaleXBy(0.1f) .duration = 400


            Handler(Looper.getMainLooper()).postDelayed({
                it.animate().scaleYBy(-0.1f).scaleXBy(-0.1f).duration = 400
                startActivity(Intent(this,IntroToApp::class.java))
            }, 300)

        }
       changelan.setOnClickListener {
           it.animate().scaleYBy(0.1f).scaleXBy(0.1f) .duration = 400


           Handler(Looper.getMainLooper()).postDelayed({
               it.animate().scaleYBy(-0.1f).scaleXBy(-0.1f).duration = 400
               showLanguageAlert()
           }, 300)

       }
        checkPermission(STORAGE_PERMISSION_CODE)
       design.setOnClickListener {
           it.animate().scaleYBy(0.1f).scaleXBy(0.1f) .duration = 400


           Handler(Looper.getMainLooper()).postDelayed({
               it.animate().scaleYBy(-0.1f).scaleXBy(-0.1f).duration = 400
               startActivity(Intent(this,MainActivity::class.java))
           }, 300)

       }
        imageButton.setOnClickListener {
            it.animate().scaleYBy(0.1f).scaleXBy(0.1f) .duration = 400


            Handler(Looper.getMainLooper()).postDelayed({
                it.animate().scaleYBy(-0.1f).scaleXBy(-0.1f).duration = 400
                startActivity(Intent(this,MainActivity::class.java))
            }, 300)

        }
        liberary.setOnClickListener {
            it.animate().scaleYBy(0.1f).scaleXBy(0.1f) .duration = 400


            Handler(Looper.getMainLooper()).postDelayed({
                it.animate().scaleYBy(-0.1f).scaleXBy(-0.1f).duration = 400
                startActivity(Intent(this,StartLibrary::class.java))
            }, 300)


        }

    }
    private fun showLanguageAlert(){
        val listItems= arrayOf("English","മലയാളം")
        val alertDialogBuilder=AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Select Language")
        alertDialogBuilder.setSingleChoiceItems(listItems,-1,object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    0->{
                        setLocale("en",0)
                        recreate()
                    }
                    1->{
                        setLocale("ml",0)
                        recreate()
                    }
                }
                dialog?.dismiss()
            }

        })
        val alertDialog=alertDialogBuilder.create()
        alertDialog.show()
    }



    private fun setLocale(s: String,from:Int) {
        sessionManager.Savelan(s)
            locale = Locale(s)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)

            flag=false

        //  startActivity(Intent(this,Interduction::class.java))

    }

    fun checkPermission( requestCode: Int) {
       val read:Int=ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val write:Int=ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        // Checking if permission is not granted
        if (read==PackageManager.PERMISSION_GRANTED &&write==PackageManager.PERMISSION_GRANTED

            )

        {


        } else {
            ActivityCompat
                .requestPermissions(
                    this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestCode
                )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            STORAGE_PERMISSION_CODE ->{
                if (grantResults.size >0){
                    if (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,
                            "Storage Permission Granted",
                            Toast.LENGTH_SHORT)
                            .show();
                    } else {
                        Toast.makeText(this,
                            "Storage Permission Denied",
                            Toast.LENGTH_SHORT)
                            .show();
                    }
                }

            }
        }
    }

}