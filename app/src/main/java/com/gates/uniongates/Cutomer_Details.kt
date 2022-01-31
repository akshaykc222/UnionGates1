package com.gates.uniongates

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.location.LocationListener
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.gates.uniongates.objects.Customer
import com.gates.uniongates.objects.SelImage
import com.gates.uniongates.objects.UserO
import com.google.android.gms.location.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cutomer__details.*
import kotlinx.android.synthetic.main.imge_shower.*
import kotlinx.android.synthetic.main.loading_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.processNextEventInCurrentThread
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class Cutomer_Details : AppCompatActivity() {
    val filePath=""
    var dowimg=""
    var frame=""
    var price=""
    val uniqu=UUID.randomUUID().toString()
    var selpanels:ArrayList<SelImage> = arrayListOf()
    private val LOCATION_PERMISSION_REQ_CODE = 1000;

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    // location updates interval - 10sec
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

    private val REQUEST_CHECK_SETTINGS = 100

    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 1
    var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cutomer__details)
        // initialize fused location client

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
      CheckPermission()

     locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS()
        } else {
            getLocation()
        }
        image_alert.visibility=View.GONE
        Glide.with(this).load(R.drawable.spinner).into(loading_gif)
        loading_View.visibility=View.GONE
        val bundle=intent.extras
        val finalimage=bundle?.getString("finalframe")
        val UriImage= Uri.fromFile(File(finalimage))

        selpanels=bundle?.getSerializable("selectedp") as ArrayList<SelImage>
         price= bundle.getString("price").toString()
         frame= bundle.getString("frame").toString()
        submit.setOnClickListener {
            hideKeyboard(this)

            image_alert.visibility=View.VISIBLE

        }
//        pincode.setOnKeyListener { v, keyCode, event ->
//            Log.d("keyovvfvf",keyCode.toString())
//            if (event.action== KeyEvent.ACTION_DOWN  && event.action == KeyEvent.KEYCODE_ENTER){
//                hideKeyboard(this)
//                image_alert.visibility=View.VISIBLE
//            }
//            return@setOnKeyListener false
//        }

        pincode.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                hideKeyboard(this)
                image_alert.visibility=View.VISIBLE
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        closeBtn.setOnClickListener {
            image_alert.visibility=View.GONE
            when {
                cname.text?.isEmpty()!! -> {
                    Toast.makeText(this,"Enter Customer name",Toast.LENGTH_LONG).show()
                }
                cphone.text.toString().length!=10 -> {
                    Toast.makeText(this,"Invalid phone number",Toast.LENGTH_LONG).show()
                }
                caddress.text?.isEmpty()!! -> {
                    Toast.makeText(this,"Enter Customer address",Toast.LENGTH_LONG).show()
                }
                agent_name.text?.isEmpty()!! -> {
                    Toast.makeText(this,"Enter agent phone number",Toast.LENGTH_LONG).show()
                }
                pincode.text?.isEmpty()!! -> {
                    Toast.makeText(this,"Enter Customer Pin code",Toast.LENGTH_LONG).show()
                }
                else -> {
                    it.isEnabled=false
                    UploadImage(UriImage)
                }
            }

        }
        val session=SessionManager(this)
        agent_name.setText(session.GetPhone())
    }
   private fun SaveDetails(User:UserO) {
       CoroutineScope(Dispatchers.IO).launch {
           val c = Customer(cname.text.toString(), cphone.text.toString(), caddress.text.toString(), agent_name.text.toString(),c_altphone.text.toString(),pincode.text.toString(),latitude,longitude)
           val dataref = FirebaseDatabase.getInstance()

           dataref.getReference("sel_panels").child(uniqu).setValue(selpanels).addOnSuccessListener {
               dataref.getReference("Customer").child(uniqu).setValue(c).addOnSuccessListener {
                   dataref.getReference("UserDesings").child(uniqu).setValue(User).addOnSuccessListener {
                       CoroutineScope(Dispatchers.Main).launch {
                         //  Toast.makeText(this, "Thank you for saving your design", Toast.LENGTH_LONG).show()
                           val intent=Intent(this@Cutomer_Details, OrderPlaced::class.java)
                           intent.putExtra("customer",c)
                           startActivity(intent)
                           loading_View.visibility=View.GONE
                       }

                   }
               }

           }
       }

    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    fun UploadImage(ImagePath:Uri){
        loading_View.visibility=View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val storageRef: FirebaseStorage = FirebaseStorage.getInstance()
            val storage=storageRef.getReference("FinalFrames/"+UUID.randomUUID().toString())
            storage.putFile(ImagePath).addOnCompleteListener{
                storage.downloadUrl.addOnSuccessListener {
                    dowimg=it.toString()
                    val sdf=SimpleDateFormat("dd-mm-yyyy hh:mm:ss", Locale.getDefault())

                    val current=sdf.format(Date())
                    val u=UserO(frame,uniqu,dowimg,price,uniqu,false,current)
                    SaveDetails(u)

                }
            }.addOnFailureListener{
                Toast.makeText(this@Cutomer_Details,"Image Uploading failed.Please try again",Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun OnGPS() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog, which -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQ_CODE
            )
        } else {
            val dis:Float= 1F
            Log.d("Customer","here")
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_DISTANCE_CHANGE_FOR_UPDATES,dis,mLocationListener)

        }
    }
    private val mLocationListener: LocationListener = object : LocationListener {


        override fun onLocationChanged(location: Location) {
            longitude=location.longitude
            latitude=location.latitude
           // Toast.makeText(this@Cutomer_Details,location.latitude.toString(),Toast.LENGTH_LONG).show()
        }
    }
    private fun CheckPermission(){
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQ_CODE
            )
        }



    }
    private fun turnGPSOn() {
        val provider: String =
            Settings.Secure.getString(contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
        if (!provider.contains("gps")) { //if gps is disabled
            val poke = Intent()
            poke.setClassName(
                "com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider"
            )
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
            poke.data = Uri.parse("3")
            sendBroadcast(poke)
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    // permission denied
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
                    );
                }
            }
        }
    }

    override fun onBackPressed() {
        if (currentFocus?.id==pincode.id){
            image_alert.visibility=View.VISIBLE
        }else{
            image_alert.visibility=View.GONE
            super.onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}