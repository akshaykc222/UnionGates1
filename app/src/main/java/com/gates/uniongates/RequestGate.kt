package com.gates.uniongates

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.se.omapi.Session
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.gates.uniongates.objects.Customer
import com.gates.uniongates.objects.UserO
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_cutomer__details.*
import kotlinx.android.synthetic.main.activity_request_gate.*
import kotlinx.android.synthetic.main.activity_request_gate.agent_name
import kotlinx.android.synthetic.main.activity_request_gate.c_altphone
import kotlinx.android.synthetic.main.activity_request_gate.caddress
import kotlinx.android.synthetic.main.activity_request_gate.cname
import kotlinx.android.synthetic.main.activity_request_gate.cphone
import kotlinx.android.synthetic.main.activity_request_gate.pincode
import kotlinx.android.synthetic.main.activity_request_gate.submit
import kotlinx.android.synthetic.main.imge_shower.*
import kotlinx.android.synthetic.main.loading_screen.*
import java.text.SimpleDateFormat
import java.util.*

class RequestGate : AppCompatActivity() {
    private val uniqu= UUID.randomUUID().toString()
    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 1
    var locationManager: LocationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_request_gate)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        CheckPermission()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS()
        } else {
            getLocation()
        }
        val sessionManager=SessionManager(this)

        agent_name.setText(sessionManager.GetPhone())
        val bundle=intent.extras
        val gateId=bundle?.getString("gateId")
        loading_View.visibility= View.GONE
        Glide.with(this).load(R.drawable.spinner).into(loading_gif)
        submit.setOnClickListener {
            image_alert.visibility=View.VISIBLE
        }
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

                else -> {
                    it.isEnabled=false
                    loading_View.visibility=View.VISIBLE
                    val sdf= SimpleDateFormat("dd-mm-yyyy hh:mm:ss", Locale.getDefault())
                    val current=sdf.format(Date())
                    val c=GateObject(gateId.toString(),uniqu,current)
                    SaveDetails(c)


                }
            }
        }
    }
   private fun hideKeyboard(activity: Activity) {
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
   private fun SaveDetails(User: GateObject) {
        val c = Customer(cname.text.toString(), cphone.text.toString(), caddress.text.toString(), agent_name.text.toString(),c_altphone.text.toString(),pincode.text.toString(),latitude, longitude)
        val dataref = FirebaseDatabase.getInstance()

            dataref.getReference("Customer").child(uniqu).setValue(c).addOnSuccessListener {
                dataref.getReference("RequestedGates").child(uniqu).setValue(User).addOnSuccessListener {
                   // Toast.makeText(this, "Thank you for saving your design", Toast.LENGTH_LONG).show()
                    val intent=Intent(this, OrderPlaced::class.java)
                    intent.putExtra("customer",c)
                    startActivity(intent)
                    loading_View.visibility= View.GONE
                }
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
}
class GateObject(val gateId:String,val custmerDetails:String,val dateTime:String)