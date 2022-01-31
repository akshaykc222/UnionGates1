package com.gates.uniongates

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import com.gates.uniongates.objects.SelImage
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_final_frame.*
import kotlinx.android.synthetic.main.activity_selected_frame2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class FinalFrame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_final_frame)
        val bundle=intent.extras
        val imageUri=bundle?.getString("finalframe")
        val selected_panels=bundle?.getSerializable("selectedp") as ArrayList<SelImage>
        val price= bundle.getString("price")
        val frame= bundle.getString("frame")
        val UriImage=Uri.fromFile(File(imageUri))

            val bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,UriImage)
            frame_original.setImageBitmap(bitmap)
            frame_mirror.setImageBitmap(bitmap)
           request.setOnClickListener {
               val bottomSheetDialog= BottomSheetDialog(this)
               bottomSheetDialog.setContentView(R.layout.custom_layout)
               bottomSheetDialog.behavior.peekHeight=500
               val proceed=bottomSheetDialog.findViewById<AppCompatButton>(R.id.proceed)
              proceed?.setOnClickListener {
                  CoroutineScope(Dispatchers.IO).launch {
                      is_account_activated(object :AccountActiveted{
                          override fun onAccountReceived(allowToOrder: Boolean) {
                              if (allowToOrder){
                                  CoroutineScope(Dispatchers.Main).launch {
                                      val outputStream: FileOutputStream?
                                      val cw=ContextWrapper(applicationContext)
                                      //  val file: File =  Environment.getExternalStorageDirectory()

                                      val dir = cw.getDir("Designs", Context.MODE_PRIVATE)
                                      dir.mkdirs()
                                      val filename = String.format("%d.png", System.currentTimeMillis())
                                      val outFile = File(dir, filename)
                                      outputStream= FileOutputStream(outFile)
                                      Log.i("akshay",outFile.toString())
                                      val bitmap1=viewToBitmap(container)
                                      bitmap1?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                                      outputStream.flush()
                                      outputStream.close()
                                      val intent= Intent(this@FinalFrame,Cutomer_Details::class.java)
                                      intent.putExtra("finalframe",imageUri)
                                      intent.putExtra("selectedp",selected_panels)
                                      intent.putExtra("price",price)
                                      intent.putExtra("frame",frame)
                                      // intent.putExtra("panels")
                                      startActivity(intent)

                                  }
                              }else{
                                  save.isEnabled=true
                                  Snackbar.make(save,"Sorry you can't order right now.Please contact admin",
                                      Snackbar.LENGTH_LONG).show()
                              }
                          }

                          override fun onError(error: String) {
                              Snackbar.make(it,error, Snackbar.LENGTH_LONG).show()
                          }

                      })
                  }
              }
                bottomSheetDialog.show()
        }
        back.setOnClickListener {
            finish()
        }


    }
   private fun viewToBitmap(view: View): Bitmap? {
        val bitmap =
            Bitmap.createBitmap(view.width, view.getHeight(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
    suspend fun is_account_activated(listener:AccountActiveted){

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
        val ref= FirebaseDatabase.getInstance().getReference("Register/$phone")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.i("Login",error.toString())
                listener.onError(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    Log.i("Login",snapshot.key.toString())

                    val json:HashMap<String,Any> = snapshot.value as HashMap<String, Any>
                    if (json.containsKey("orderAllowed")){
                        if (json["isApproved"]==true && json["orderAllowed"]==true){
                            listener.onAccountReceived(true)
                        }else{
                            listener.onAccountReceived(false)
                        }
                    }else{
                        listener.onAccountReceived(false)
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
                    listener.onError("No data found")
                }
            }

        })

    }
}